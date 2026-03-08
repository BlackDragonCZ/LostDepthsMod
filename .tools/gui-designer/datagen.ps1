# GUI Designer Registry Datagen
# Scans project Menu and Screen classes to generate registry.json
# Usage: powershell -ExecutionPolicy Bypass -File datagen.ps1

$projectRoot = (Resolve-Path "$PSScriptRoot\..\..").Path
$menuDir = "$projectRoot\src\main\java\cz\blackdragoncz\lostdepths\world\inventory"
$screenDir = "$projectRoot\src\main\java\cz\blackdragoncz\lostdepths\client\gui"
$menusFile = "$projectRoot\src\main\java\cz\blackdragoncz\lostdepths\init\LostdepthsModMenus.java"
$screensFile = "$projectRoot\src\main\java\cz\blackdragoncz\lostdepths\init\LostdepthsModScreens.java"
$outputFile = "$PSScriptRoot\registry.json"

Write-Host "GUI Designer Datagen" -ForegroundColor Cyan
Write-Host "Project: $projectRoot"
Write-Host ""

# Helper: try to evaluate simple integer expressions like "8 + 2 * 18" or "42 + 84"
function TryEvalExpr($expr) {
    $expr = $expr.Trim()
    # Pure number
    if ($expr -match '^\d+$') { return [int]$expr }
    # Simple math with +, -, *, / and numbers only
    $clean = $expr -replace '[^0-9+\-*/() ]', ''
    if ($clean -eq $expr) {
        try {
            $result = Invoke-Expression $clean
            if ($result -is [int] -or $result -is [double]) { return [int]$result }
        } catch {}
    }
    return $null
}

# Step 1: Parse LostdepthsModMenus.java
$menusContent = Get-Content $menusFile -Raw
$menuRegistrations = [regex]::Matches($menusContent, 'RegistryObject<MenuType<(\w+)>>\s+(\w+)\s*=\s*REGISTRY\.register\("([^"]+)"')

# Step 2: Parse LostdepthsModScreens.java
$screensContent = Get-Content $screensFile -Raw
$screenRegistrations = [regex]::Matches($screensContent, 'MenuScreens\.register\(LostdepthsModMenus\.(\w+)\.get\(\),\s*(\w+)::new\)')

$screenLookup = @{}
foreach ($match in $screenRegistrations) {
    $screenLookup[$match.Groups[1].Value] = $match.Groups[2].Value
}

$guis = @()

foreach ($reg in $menuRegistrations) {
    $menuClass = $reg.Groups[1].Value
    $menuField = $reg.Groups[2].Value
    $registryId = $reg.Groups[3].Value

    Write-Host "Processing: $registryId ($menuClass)" -ForegroundColor Yellow

    $screenClass = $screenLookup[$menuField]
    if (-not $screenClass) {
        Write-Host "  WARNING: No screen found for $menuField, skipping" -ForegroundColor Red
        continue
    }

    $menuFile = Get-ChildItem "$menuDir\$menuClass.java" -ErrorAction SilentlyContinue
    $screenFile = Get-ChildItem "$screenDir\$screenClass.java" -ErrorAction SilentlyContinue
    if (-not $menuFile -or -not $screenFile) {
        Write-Host "  WARNING: Source file not found, skipping" -ForegroundColor Red
        continue
    }

    $menuContent = Get-Content $menuFile.FullName -Raw
    $screenContent = Get-Content $screenFile.FullName -Raw

    # Generate label
    $labelParts = ($registryId -replace '_menu$','' -replace '_gui$','') -split '_'
    $label = ($labelParts | ForEach-Object { $_.Substring(0,1).ToUpper() + $_.Substring(1) }) -join ' '

    # Extract imageWidth/imageHeight from screen
    $imgW = 176; $imgH = 166
    if ($screenContent -match 'imageWidth\s*=\s*(\d+)') { $imgW = [int]$Matches[1] }
    if ($screenContent -match 'imageHeight\s*=\s*(\d+)') { $imgH = [int]$Matches[1] }

    # Extract background texture
    $bgTexture = "unknown"
    if ($screenContent -match 'rl\("textures/(?:gui|screens)/([^"]+)\.png"\)') {
        $bgTexture = $Matches[1]
    } elseif ($screenContent -match 'lostdepths:textures/(?:gui|screens)/([^"]+)\.png') {
        $bgTexture = $Matches[1]
    } elseif ($screenContent -match '"textures/(?:gui|screens)/([^"]+)\.png"') {
        $bgTexture = $Matches[1]
    }

    $elements = @()

    # ===== RESOLVE LOCAL CONSTANTS =====
    # Build a dictionary of constant name -> value from the menu file
    $constants = @{}
    # Match: int/final int/static final int NAME = VALUE;
    $constMatches = [regex]::Matches($menuContent, '(?:static\s+final\s+)?(?:final\s+)?int\s+(\w+)\s*=\s*(\d+)\s*;')
    foreach ($cm in $constMatches) {
        $constants[$cm.Groups[1].Value] = [int]$cm.Groups[2].Value
    }
    # Also resolve constants from referenced Screen class (e.g. NurostarGeneratorScreen.SLOT_SIZE)
    $screenConstants = [regex]::Matches($screenContent, '(?:static\s+final\s+)?(?:final\s+)?int\s+(\w+)\s*=\s*(\d+)\s*;')
    foreach ($sc in $screenConstants) {
        $fullKey = "$screenClass.$($sc.Groups[1].Value)"
        $constants[$fullKey] = [int]$sc.Groups[2].Value
        # Also store plain name if not already present
        if (-not $constants.ContainsKey($sc.Groups[1].Value)) {
            $constants[$sc.Groups[1].Value] = [int]$sc.Groups[2].Value
        }
    }
    # Second pass: resolve constants that reference other constants (e.g. twoSlotsWidth = (SLOT_SIZE * 2) + 5)
    $exprConstMatches = [regex]::Matches($menuContent, '(?:static\s+final\s+)?(?:final\s+)?int\s+(\w+)\s*=\s*([^;]+);')
    foreach ($ec in $exprConstMatches) {
        $name = $ec.Groups[1].Value
        if ($constants.ContainsKey($name)) { continue }
        $expr = $ec.Groups[2].Value.Trim()
        # Try to resolve by substituting known constants
        $resolved = $expr
        foreach ($k in ($constants.Keys | Sort-Object -Descending { $_.Length })) {
            $resolved = $resolved -replace [regex]::Escape($k), $constants[$k]
        }
        $clean = $resolved -replace '[^0-9+\-*/() ]', ''
        if ($clean.Length -gt 0 -and $clean -eq $resolved.Trim()) {
            try {
                $val = [int](Invoke-Expression $clean)
                $constants[$name] = $val
            } catch {}
        }
    }

    # Helper: resolve a value that might be a constant name or literal number
    function ResolveVal($val, $consts) {
        $val = $val.Trim()
        if ($val -match '^\d+$') { return [int]$val }
        if ($consts.ContainsKey($val)) { return $consts[$val] }
        # Try simple math with resolved constants
        $resolved = $val
        foreach ($k in ($consts.Keys | Sort-Object -Descending { $_.Length })) {
            $resolved = $resolved -replace [regex]::Escape($k), $consts[$k]
        }
        $clean = $resolved -replace '[^0-9+\-*/() ]', ''
        if ($clean.Length -gt 0 -and $clean -eq $resolved) {
            try { return [int](Invoke-Expression $clean) } catch {}
        }
        return $null
    }

    # ===== EXTRACT SLOTS =====
    # Broad pattern: SlotItemHandler(anything, INDEX_OR_CONST, X_EXPR, Y_EXPR)
    # Must handle: literal ints, constant names, expressions, trailing ) or ) {
    $slotPattern = 'new\s+SlotItemHandler\([^,]+,\s*([^,]+),\s*([^,]+),\s*([^)]+)\)'
    $slotMatches = [regex]::Matches($menuContent, $slotPattern)
    $seenSlots = @{}
    foreach ($s in $slotMatches) {
        $idxRaw = $s.Groups[1].Value.Trim()
        $xRaw = $s.Groups[2].Value.Trim()
        $yRaw = $s.Groups[3].Value.Trim()

        $idx = ResolveVal $idxRaw $constants
        $sx = ResolveVal $xRaw $constants
        $sy = ResolveVal $yRaw $constants

        if ($null -eq $idx) { $idx = $seenSlots.Count }
        if ($null -eq $sx -or $null -eq $sy) {
            Write-Host "    WARN: Cannot resolve slot($idxRaw, $xRaw, $yRaw)" -ForegroundColor DarkYellow
            continue
        }

        $slotKey = "$idx,$sx,$sy"
        if ($seenSlots.ContainsKey($slotKey)) { continue }
        $seenSlots[$slotKey] = $true

        $elements += [ordered]@{
            type = "slot"
            id = "slot_$idx"
            label = "Slot $idx"
            slotIndex = [int]$idx
            x = [int]$sx
            y = [int]$sy
        }
    }

    # Match Slot(containerWrapper, index, X + expr, Y + expr) with base literal x,y
    $containerSlotPattern = 'new\s+Slot\(containerWrapper,\s*\w+,\s*(\d+)\s*(?:\+[^,]*)?,\s*(\d+)\s*(?:\+[^)]*?)?\)'
    $containerSlotMatches = [regex]::Matches($menuContent, $containerSlotPattern)
    $csIdx = 100
    foreach ($s in $containerSlotMatches) {
        $sx = [int]$s.Groups[1].Value
        $sy = [int]$s.Groups[2].Value
        $elements += [ordered]@{
            type = "slot"
            id = "craft_slot_$csIdx"
            label = "Craft $csIdx"
            slotIndex = $csIdx
            x = $sx
            y = $sy
        }
        $csIdx++
    }

    # Match CustomResultSlot: last 3 numeric params are index, x, y
    $resultSlotPattern = 'new\s+CustomResultSlot[^(]*\([^,]+,\s*[^,]+,\s*[^,]+,\s*[^,]+,\s*[^,]+,\s*(\d+),\s*(\d+),\s*(\d+)\)'
    $resultSlotMatch = [regex]::Match($menuContent, $resultSlotPattern)
    if ($resultSlotMatch.Success) {
        $elements += [ordered]@{
            type = "slot"
            id = "result"
            label = "Result"
            slotIndex = [int]$resultSlotMatch.Groups[1].Value
            x = [int]$resultSlotMatch.Groups[2].Value
            y = [int]$resultSlotMatch.Groups[3].Value
        }
    }

    # ===== PLAYER INVENTORY =====
    # Match various patterns for player inventory slots
    $piFound = $false

    # Pattern 1: Slot(inv/playerInventory, ..., X + col * 18, Y + row * 18)
    $piPatterns = @(
        'new\s+Slot\(\s*(?:inv|playerInventory)\s*,\s*[^,]+,\s*(\d+)\s*\+\s*\w+\s*\*\s*18\s*,\s*(\d+)\s*\+\s*\w+\s*\*\s*18\s*\)',
        'new\s+Slot\(\s*inv\s*,.*?,\s*(?:0\s*\+\s*)?(\d+)\s*\+\s*\w+\s*\*\s*18\s*,\s*(?:0\s*\+\s*)?(\d+)\s*\+\s*\w+\s*\*\s*18\s*\)',
        'new\s+Slot\(\s*inv\s*,\s*\w+\s*\+\s*\(\s*\w+\s*\+\s*\d+\)\s*\*\s*\d+\s*,\s*(\d+)\s*\+\s*\w+\s*\*\s*18\s*,\s*(\d+)\s*\+\s*\w+\s*\*\s*18\s*\)'
    )
    foreach ($pip in $piPatterns) {
        if ($piFound) { break }
        $piMatch = [regex]::Match($menuContent, $pip)
        if ($piMatch.Success) {
            $elements += [ordered]@{
                type = "player_inventory"
                id = "player_inv"
                x = [int]$piMatch.Groups[1].Value
                y = [int]$piMatch.Groups[2].Value
            }
            $piFound = $true
        }
    }

    # ===== LABELS from renderLabels =====
    $labelMatches = [regex]::Matches($screenContent, 'drawString\([^,]+,\s*"([^"]+)"\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(-?\d+|0x[0-9a-fA-F]+)')
    $labelIdx = 0
    foreach ($lm in $labelMatches) {
        $elements += [ordered]@{
            type = "label"
            id = "label_$labelIdx"
            text = $lm.Groups[1].Value
            x = [int]$lm.Groups[2].Value
            y = [int]$lm.Groups[3].Value
            color = $lm.Groups[4].Value
        }
        $labelIdx++
    }

    # ===== PROGRESS BARS from screen =====
    $progXMatch = [regex]::Match($screenContent, 'PROGRESS_X\s*=\s*(\d+)')
    $progYMatch = [regex]::Match($screenContent, 'PROGRESS_Y\s*=\s*(\d+)')
    if ($progXMatch.Success -and $progYMatch.Success) {
        $pw = 24; $ph = 17
        $pwMatch = [regex]::Match($screenContent, 'PROGRESS_WIDTH\s*=\s*(\d+)')
        $phMatch = [regex]::Match($screenContent, 'PROGRESS.*?HEIGHT\s*=\s*(\d+)')
        if ($pwMatch.Success) { $pw = [int]$pwMatch.Groups[1].Value }
        if ($phMatch.Success) { $ph = [int]$phMatch.Groups[1].Value }
        $elements += [ordered]@{
            type = "progress"
            id = "progress"
            x = [int]$progXMatch.Groups[1].Value
            y = [int]$progYMatch.Groups[1].Value
            w = $pw
            h = $ph
        }
    }

    # ===== BLIT calls from screen =====
    # Match: g.blit(TEXTURE, leftPos + X, topPos + Y, UVX, UVY, W, H)
    $blitPattern = 'g\.blit\(\s*TEXTURE\s*,\s*leftPos\s*\+\s*(\d+)\s*,\s*topPos\s*\+\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)'
    $blitMatches = [regex]::Matches($screenContent, $blitPattern)
    $blitIdx = 0
    foreach ($bm in $blitMatches) {
        # Skip the main background blit (0, 0, imageWidth, imageHeight)
        $bx = [int]$bm.Groups[1].Value; $by = [int]$bm.Groups[2].Value
        $buvx = [int]$bm.Groups[3].Value; $buvy = [int]$bm.Groups[4].Value
        $bw = [int]$bm.Groups[5].Value; $bh = [int]$bm.Groups[6].Value
        if ($buvx -eq 0 -and $buvy -eq 0 -and $bw -eq $imgW -and $bh -eq $imgH) { continue }
        $elements += [ordered]@{
            type = "blit"
            id = "blit_$blitIdx"
            label = "Blit $blitIdx"
            x = $bx
            y = $by
            w = $bw
            h = $bh
            uvX = $buvx
            uvY = $buvy
            texW = 256
            texH = 256
        }
        $blitIdx++
    }

    # ===== ENERGY BAR from screen =====
    # Look for energy-related rendering constants
    $eXMatch = [regex]::Match($screenContent, '(?:ENERGY|energy).*?[Xx]\s*=\s*(\d+)')
    $eYMatch = [regex]::Match($screenContent, '(?:ENERGY|energy).*?[Yy]\s*=\s*(\d+)')
    if ($eXMatch.Success -and $eYMatch.Success) {
        $ew = 20; $eh = 60
        $ewMatch = [regex]::Match($screenContent, '(?:ENERGY|energy).*?[Ww](?:idth)?\s*=\s*(\d+)')
        $ehMatch = [regex]::Match($screenContent, '(?:ENERGY|energy).*?[Hh](?:eight)?\s*=\s*(\d+)')
        if ($ewMatch.Success) { $ew = [int]$ewMatch.Groups[1].Value }
        if ($ehMatch.Success) { $eh = [int]$ehMatch.Groups[1].Value }
        $elements += [ordered]@{
            type = "energy_bar"
            id = "energy"
            x = [int]$eXMatch.Groups[1].Value
            y = [int]$eYMatch.Groups[1].Value
            w = $ew
            h = $eh
        }
    }

    # Build entry
    $menuRelPath = $menuFile.FullName.Replace($projectRoot + '\', '').Replace('\', '/')
    $screenRelPath = $screenFile.FullName.Replace($projectRoot + '\', '').Replace('\', '/')

    $guiEntry = [ordered]@{
        id = "lostdepths:$registryId"
        label = $label
        screenPath = $screenRelPath
        menuPath = $menuRelPath
        imageWidth = $imgW
        imageHeight = $imgH
        bgTexture = $bgTexture
        elements = $elements
    }

    $guis += $guiEntry
    Write-Host "  OK: $($elements.Count) elements found" -ForegroundColor Green
}

# Build and write JSON
$registry = [ordered]@{ guis = $guis }
$json = $registry | ConvertTo-Json -Depth 5
$json = $json -replace '\\/', '/'
[System.IO.File]::WriteAllText($outputFile, $json, (New-Object System.Text.UTF8Encoding $false))

Write-Host ""
Write-Host "Generated: $outputFile" -ForegroundColor Cyan
Write-Host "Total GUIs: $($guis.Count)" -ForegroundColor Cyan
