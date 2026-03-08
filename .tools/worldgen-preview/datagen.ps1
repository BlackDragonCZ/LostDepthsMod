# Worldgen Preview - Block Color Datagen
# Scans block models to find textures, extracts average RGB color from each texture
# Outputs block-colors.json mapping block name -> [r, g, b] (0.0-1.0)
# Usage: powershell -ExecutionPolicy Bypass -File datagen.ps1

$projectRoot = (Resolve-Path "$PSScriptRoot\..\..").Path
$modelsDir = "$projectRoot\src\main\resources\assets\lostdepths\models\block"
$texturesDir = "$projectRoot\src\main\resources\assets\lostdepths\textures"
$blockstatesDir = "$projectRoot\src\main\resources\assets\lostdepths\blockstates"
$outputFile = "$PSScriptRoot\block-colors.json"

Add-Type -AssemblyName System.Drawing

Write-Host "Worldgen Preview - Block Color Datagen" -ForegroundColor Cyan
Write-Host "Project: $projectRoot"
Write-Host ""

# Helper: extract average color from a PNG file (first frame only for animated textures)
function Get-AverageColor($pngPath) {
    if (-not (Test-Path $pngPath)) { return $null }
    try {
        $bitmap = [System.Drawing.Bitmap]::new($pngPath)
        $w = $bitmap.Width
        # For animated textures, only use first frame (square region)
        $h = [Math]::Min($bitmap.Height, $w)

        $totalR = 0; $totalG = 0; $totalB = 0; $count = 0
        for ($y = 0; $y -lt $h; $y++) {
            for ($x = 0; $x -lt $w; $x++) {
                $pixel = $bitmap.GetPixel($x, $y)
                if ($pixel.A -lt 128) { continue } # Skip transparent pixels
                $totalR += $pixel.R
                $totalG += $pixel.G
                $totalB += $pixel.B
                $count++
            }
        }
        $bitmap.Dispose()

        if ($count -eq 0) { return $null }
        $r = [Math]::Round($totalR / $count / 255.0, 3)
        $g = [Math]::Round($totalG / $count / 255.0, 3)
        $b = [Math]::Round($totalB / $count / 255.0, 3)
        return @($r, $g, $b)
    } catch {
        Write-Host "    WARN: Failed to read $pngPath - $($_.Exception.Message)" -ForegroundColor DarkYellow
        return $null
    }
}

# Helper: resolve texture reference to a PNG file path
# Input: "lostdepths:block/deep_rock" or "minecraft:block/stone" or just "block/deep_rock"
function Resolve-TexturePath($ref) {
    if (-not $ref) { return $null }
    $ref = $ref.Trim()

    if ($ref.StartsWith("minecraft:")) {
        # Vanilla texture - skip, we'll use hardcoded colors for vanilla
        return $null
    }

    # Strip namespace
    if ($ref.StartsWith("lostdepths:")) {
        $ref = $ref.Substring("lostdepths:".Length)
    }

    $path = "$texturesDir\$($ref.Replace('/', '\')).png"
    if (Test-Path $path) { return $path }
    return $null
}

# Resolve texture from a block model JSON
function Get-BlockTexture($modelPath) {
    if (-not (Test-Path $modelPath)) { return $null }
    try {
        $json = Get-Content $modelPath -Raw | ConvertFrom-Json
        $textures = $json.textures

        if (-not $textures) { return $null }

        # Priority: all > top > side > particle > first texture found
        $ref = $null
        if ($textures.all) { $ref = $textures.all }
        elseif ($textures.top) { $ref = $textures.top }
        elseif ($textures.side) { $ref = $textures.side }
        elseif ($textures.particle) { $ref = $textures.particle }
        else {
            # Get first property
            $textures.PSObject.Properties | Select-Object -First 1 | ForEach-Object {
                $ref = $_.Value
            }
        }

        return $ref
    } catch {
        return $null
    }
}

$colors = [ordered]@{}

# Vanilla block defaults (hardcoded since we can't read vanilla textures)
$vanillaColors = @{
    "minecraft:air" = @(0, 0, 0)
    "minecraft:stone" = @(0.5, 0.5, 0.5)
    "minecraft:deepslate" = @(0.33, 0.33, 0.37)
    "minecraft:bedrock" = @(0.2, 0.2, 0.2)
    "minecraft:water" = @(0.2, 0.3, 0.9)
    "minecraft:lava" = @(0.9, 0.3, 0.0)
    "minecraft:sand" = @(0.85, 0.82, 0.65)
    "minecraft:gravel" = @(0.55, 0.53, 0.53)
    "minecraft:dirt" = @(0.55, 0.38, 0.25)
    "minecraft:grass_block" = @(0.4, 0.65, 0.25)
    "minecraft:netherrack" = @(0.45, 0.2, 0.2)
    "minecraft:soul_sand" = @(0.33, 0.25, 0.2)
    "minecraft:end_stone" = @(0.85, 0.85, 0.6)
    "minecraft:obsidian" = @(0.1, 0.05, 0.15)
    "minecraft:ice" = @(0.6, 0.75, 0.95)
    "minecraft:snow_block" = @(0.95, 0.95, 0.95)
    "minecraft:clay" = @(0.6, 0.62, 0.67)
    "minecraft:terracotta" = @(0.6, 0.4, 0.3)
}

foreach ($k in $vanillaColors.Keys) {
    $colors[$k] = $vanillaColors[$k]
}

Write-Host "Added $($vanillaColors.Count) vanilla block colors" -ForegroundColor Green

# Scan all block models in the mod
$modelFiles = Get-ChildItem "$modelsDir\*.json" -ErrorAction SilentlyContinue
$processed = 0
$found = 0

foreach ($modelFile in $modelFiles) {
    $blockName = "lostdepths:" + $modelFile.BaseName
    $processed++

    $texRef = Get-BlockTexture $modelFile.FullName
    if (-not $texRef) {
        # Try particle as fallback
        continue
    }

    $pngPath = Resolve-TexturePath $texRef
    if (-not $pngPath) { continue }

    $color = Get-AverageColor $pngPath
    if ($color) {
        $colors[$blockName] = $color
        $found++
        Write-Host "  $blockName -> $texRef -> [$($color -join ', ')]" -ForegroundColor DarkGray
    }
}

Write-Host ""
Write-Host "Processed $processed models, extracted $found colors" -ForegroundColor Yellow

# Build JSON output
$jsonObj = [ordered]@{}
foreach ($k in $colors.Keys) {
    $jsonObj[$k] = $colors[$k]
}

$json = $jsonObj | ConvertTo-Json -Depth 3
[System.IO.File]::WriteAllText($outputFile, $json, (New-Object System.Text.UTF8Encoding $false))

Write-Host ""
Write-Host "Generated: $outputFile" -ForegroundColor Cyan
Write-Host "Total block colors: $($colors.Count)" -ForegroundColor Cyan
