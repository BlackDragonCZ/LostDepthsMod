# JEI Designer Registry Datagen
# Scans JEI category classes and recipe JSONs to generate registry.json
# Usage: powershell -ExecutionPolicy Bypass -File datagen.ps1

$projectRoot = (Resolve-Path "$PSScriptRoot\..\..").Path
$jeiDir = "$projectRoot\src\main\java\cz\blackdragoncz\lostdepths\client\jei"
$recipesDir = "$projectRoot\src\main\resources\data\lostdepths\recipes"
$recipeTypeFile = "$jeiDir\RecipeViewerRecipeType.java"
$pluginFile = "$jeiDir\LostDepthsJEI.java"
$outputFile = "$PSScriptRoot\registry.json"

$guiTexturesFile = "$projectRoot\src\main\java\cz\blackdragoncz\lostdepths\client\gui\AllGuiTextures.java"
$textureBase = "$projectRoot\src\main\resources\assets\lostdepths\textures"

Write-Host "JEI Designer Datagen" -ForegroundColor Cyan
Write-Host "Project: $projectRoot"
Write-Host ""

# ── Step 0: Collect all available textures ──

# 0a: Parse AllGuiTextures.java for widget sprites
$guiTexContent = Get-Content $guiTexturesFile -Raw
$widgetSprites = @()

# Match entries like: JEI_SLOT("jei/widgets", 18, 18) or JEI_CHANCE_SLOT("jei/widgets", 20, 156, 18, 18)
$spritePattern = '(\w+)\(\s*"([^"]+)"\s*,\s*(\d+)\s*,\s*(\d+)\s*(?:,\s*(\d+)\s*,\s*(\d+)\s*)?\)'
$spriteMatches = [regex]::Matches($guiTexContent, $spritePattern)

foreach ($sm in $spriteMatches) {
    $spriteName = $sm.Groups[1].Value
    $spriteSheet = $sm.Groups[2].Value
    if ($sm.Groups[5].Success) {
        # 5-arg: (sheet, startX, startY, width, height)
        $widgetSprites += [ordered]@{
            name = $spriteName
            sheet = "textures/gui/${spriteSheet}.png"
            servePath = "/textures/gui/${spriteSheet}.png"
            startX = [int]$sm.Groups[3].Value
            startY = [int]$sm.Groups[4].Value
            width = [int]$sm.Groups[5].Value
            height = [int]$sm.Groups[6].Value
        }
    } else {
        # 3-arg: (sheet, width, height) -> startX=0, startY=0
        $widgetSprites += [ordered]@{
            name = $spriteName
            sheet = "textures/gui/${spriteSheet}.png"
            servePath = "/textures/gui/${spriteSheet}.png"
            startX = 0
            startY = 0
            width = [int]$sm.Groups[3].Value
            height = [int]$sm.Groups[4].Value
        }
    }
}
Write-Host "Found $($widgetSprites.Count) widget sprites from AllGuiTextures" -ForegroundColor Green

# 0b: Scan texture directories for all available PNGs
$availableTextures = @()
$texFolders = @("screens", "gui", "gui/jei", "gui/guidebook")
foreach ($folder in $texFolders) {
    $dir = "$textureBase\$($folder.Replace('/', '\'))"
    if (Test-Path $dir) {
        Get-ChildItem "$dir\*.png" -ErrorAction SilentlyContinue | ForEach-Object {
            $relFolder = $folder.Replace('\', '/')
            $availableTextures += [ordered]@{
                name = $_.BaseName
                folder = $relFolder
                path = "textures/$relFolder/$($_.Name)"
                servePath = "/textures/$relFolder/$($_.Name)"
                fileSize = $_.Length
            }
        }
    }
}
Write-Host "Found $($availableTextures.Count) texture files" -ForegroundColor Green

# Step 1: Parse RecipeViewerRecipeType.java to get category metadata
$rtContent = Get-Content $recipeTypeFile -Raw

# Match: public static final RecipeTypeWrapper<...> NAME = new RecipeTypeWrapper<>(..., CLASS.class, X, Y, W, H, new IItemProvider() { ... return ITEMS.ITEM.get(); });
$rtPattern = 'public\s+static\s+final\s+RecipeTypeWrapper<[^>]*>\s+(\w+)\s*=\s*new\s+RecipeTypeWrapper<>\(\s*LostDepthsModRecipeType\.(\w+)\.get\(\)\s*,\s*\w+\.class\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)'
$rtMatches = [regex]::Matches($rtContent, $rtPattern)

$categories = @()

foreach ($m in $rtMatches) {
    $fieldName = $m.Groups[1].Value
    $recipeTypeName = $m.Groups[2].Value
    $xOff = [int]$m.Groups[3].Value
    $yOff = [int]$m.Groups[4].Value
    $width = [int]$m.Groups[5].Value
    $height = [int]$m.Groups[6].Value

    Write-Host "Found category: $fieldName ($width x $height)" -ForegroundColor Yellow

    # Find the category class from LostDepthsJEI.java
    $pluginContent = Get-Content $pluginFile -Raw
    $catClassPattern = "new\s+(\w+Category)\s*\(\s*\w+\s*,\s*RecipeViewerRecipeType\.$fieldName"
    $catClassMatch = [regex]::Match($pluginContent, $catClassPattern)
    $categoryClass = if ($catClassMatch.Success) { $catClassMatch.Groups[1].Value } else { "Unknown" }

    # Find the category file
    $catFile = Get-ChildItem "$jeiDir" -Recurse -Filter "$categoryClass.java" -ErrorAction SilentlyContinue | Select-Object -First 1
    $catContent = if ($catFile) { Get-Content $catFile.FullName -Raw } else { "" }
    $catRelPath = if ($catFile) { $catFile.FullName.Replace($projectRoot + '\', '').Replace('\', '/') } else { "" }

    # Extract elements from category class
    $elements = @()

    if ($catContent) {
        # Extract slot.draw(guiGraphics, X, Y) calls
        $slotDrawPattern = 'slot\.draw\(\s*\w+\s*,\s*(\d+)\s*,\s*(\d+)\s*\)'
        $slotDrawMatches = [regex]::Matches($catContent, $slotDrawPattern)
        $slotIdx = 0
        foreach ($sd in $slotDrawMatches) {
            $elements += [ordered]@{
                type = "slot_bg"
                id = "slot_bg_$slotIdx"
                x = [int]$sd.Groups[1].Value
                y = [int]$sd.Groups[2].Value
            }
            $slotIdx++
        }

        # Extract addSlot positions from setRecipe
        $addSlotPattern = 'addSlot\(\s*RecipeIngredientRole\.(\w+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)'
        $addSlotMatches = [regex]::Matches($catContent, $addSlotPattern)
        $recipeSlotIdx = 0
        foreach ($as in $addSlotMatches) {
            $elements += [ordered]@{
                type = "recipe_slot"
                id = "recipe_slot_$recipeSlotIdx"
                role = $as.Groups[1].Value
                x = [int]$as.Groups[2].Value
                y = [int]$as.Groups[3].Value
            }
            $recipeSlotIdx++
        }

        # Extract arrow position
        $arrowBgPattern = 'arrowBg\.draw\(\s*\w+\s*,\s*(\d+)\s*,\s*(\d+)\s*\)'
        $arrowBgMatch = [regex]::Match($catContent, $arrowBgPattern)
        if ($arrowBgMatch.Success) {
            $aw = 24; $ah = 17
            $awMatch = [regex]::Match($catContent, 'arrowWidth\s*=\s*(\d+)')
            $ahMatch = [regex]::Match($catContent, 'arrowHeight\s*=\s*(\d+)')
            if ($awMatch.Success) { $aw = [int]$awMatch.Groups[1].Value }
            if ($ahMatch.Success) { $ah = [int]$ahMatch.Groups[1].Value }
            $elements += [ordered]@{
                type = "arrow"
                id = "arrow"
                x = [int]$arrowBgMatch.Groups[1].Value
                y = [int]$arrowBgMatch.Groups[2].Value
                w = $aw
                h = $ah
            }
        }

        # Extract background texture blit
        $bgTexPattern = 'drawableBuilder\(\s*texture\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)\s*\.setTextureSize\(\s*(\d+)\s*,\s*(\d+)\s*\)'
        $bgTexMatch = [regex]::Match($catContent, $bgTexPattern)
        if ($bgTexMatch.Success) {
            $elements += [ordered]@{
                type = "bg_texture"
                id = "bg_texture"
                uvX = [int]$bgTexMatch.Groups[1].Value
                uvY = [int]$bgTexMatch.Groups[2].Value
                w = [int]$bgTexMatch.Groups[3].Value
                h = [int]$bgTexMatch.Groups[4].Value
                texW = [int]$bgTexMatch.Groups[5].Value
                texH = [int]$bgTexMatch.Groups[6].Value
            }
        }

        # Extract power icon
        $powerPattern = 'powerIcon\.draw\(\s*\w+\s*,\s*(\d+)\s*,'
        $powerMatch = [regex]::Match($catContent, $powerPattern)
        if ($powerMatch.Success) {
            $elements += [ordered]@{
                type = "power_icon"
                id = "power_icon"
                x = [int]$powerMatch.Groups[1].Value
                y = 0
            }
        }

        # Extract texture ResourceLocation
        $texLocPattern = 'ResourceLocation\(\s*"([^"]+textures/[^"]+)"'
        $texLocMatch = [regex]::Match($catContent, $texLocPattern)
        $bgTexturePath = if ($texLocMatch.Success) { $texLocMatch.Groups[1].Value } else { "" }

        # Extract ALL ResourceLocation texture references used in this category
        $catTextures = @()

        # Pattern 1: new ResourceLocation("lostdepths:textures/...") or ("textures/...")
        $resLocPattern = 'new\s+ResourceLocation\(\s*"([^"]*textures/[^"]+)"'
        $resLocMatches = [regex]::Matches($catContent, $resLocPattern)
        foreach ($rl in $resLocMatches) {
            $texPath = $rl.Groups[1].Value
            # Normalize: strip namespace prefix
            $texPath = $texPath -replace '^lostdepths:', ''
            $servePath = "/" + $texPath
            $catTextures += [ordered]@{
                source = "ResourceLocation"
                path = $texPath
                servePath = $servePath
            }
        }

        # Pattern 2: LostdepthsMod.rl("textures/...")
        $rlPattern = 'LostdepthsMod\.rl\(\s*"(textures/[^"]+)"'
        $rlMatches = [regex]::Matches($catContent, $rlPattern)
        foreach ($rl in $rlMatches) {
            $catTextures += [ordered]@{
                source = "LostdepthsMod.rl"
                path = $rl.Groups[1].Value
                servePath = "/" + $rl.Groups[1].Value
            }
        }

        # Pattern 3: drawableBuilder(..., uvX, uvY, w, h) — extract UV regions from any drawable
        $dbPattern = 'drawableBuilder\(\s*(?:new\s+ResourceLocation\(\s*"([^"]+)"\s*\)|[^,]+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)'
        $dbMatches = [regex]::Matches($catContent, $dbPattern)
        foreach ($db in $dbMatches) {
            $texRef = if ($db.Groups[1].Success) { $db.Groups[1].Value -replace '^lostdepths:', '' } else { "" }
            $catTextures += [ordered]@{
                source = "drawableBuilder"
                texturePath = $texRef
                uvX = [int]$db.Groups[2].Value
                uvY = [int]$db.Groups[3].Value
                uvW = [int]$db.Groups[4].Value
                uvH = [int]$db.Groups[5].Value
            }
        }

        # Pattern 4: AllGuiTextures references used in this category (e.g., AllGuiTextures.JEI_SHADOW)
        $guiTexRefPattern = 'AllGuiTextures\.(\w+)'
        $guiTexRefMatches = [regex]::Matches($catContent, $guiTexRefPattern)
        $seenGuiTex = @{}
        foreach ($gt in $guiTexRefMatches) {
            $name = $gt.Groups[1].Value
            if (-not $seenGuiTex.ContainsKey($name)) {
                $seenGuiTex[$name] = $true
                # Find matching widget sprite
                $sprite = $widgetSprites | Where-Object { $_.name -eq $name } | Select-Object -First 1
                if ($sprite) {
                    $catTextures += [ordered]@{
                        source = "AllGuiTextures"
                        name = $name
                        sheet = $sprite.sheet
                        servePath = $sprite.servePath
                        startX = $sprite.startX
                        startY = $sprite.startY
                        width = $sprite.width
                        height = $sprite.height
                    }
                }
            }
        }
    }

    # Step 2: Find recipes for this category
    $recipes = @()
    $recipeTypeLower = $recipeTypeName.ToLower()

    # Map recipe type names to recipe directories
    $recipeDirMap = @{
        "V1_COMPRESSING" = "compressing/v1"
        "V2_COMPRESSING" = "compressing/v2"
        "V3_COMPRESSING" = "compressing/v3"
        "GALACTIC_WORKSTATION" = "galactic_workstation"
        "ALLOY_WORKSTATION" = "alloy_workstation"
        "MODULE_CREATOR" = "module_creator"
        "ITEM_USE" = "item_use"
        "META_MATERIALIZER" = "meta_materializer"
        "FUSION_TABLE" = "fusion_table"
    }

    $recipeSubDir = $recipeDirMap[$fieldName]
    if ($recipeSubDir) {
        $fullRecipeDir = "$recipesDir\$recipeSubDir"
        if (Test-Path $fullRecipeDir) {
            $recipeFiles = Get-ChildItem "$fullRecipeDir\*.json" -ErrorAction SilentlyContinue
            foreach ($rf in $recipeFiles) {
                try {
                    $recipeJson = Get-Content $rf.FullName -Raw | ConvertFrom-Json
                    $recipeEntry = [ordered]@{
                        file = $rf.Name
                        path = $rf.FullName.Replace($projectRoot + '\', '').Replace('\', '/')
                    }

                    # Extract items based on recipe type
                    $recipeType = $recipeJson.type
                    if ($recipeJson.input) {
                        $recipeEntry.input = $recipeJson.input.item
                    }
                    if ($recipeJson.input1) {
                        $recipeEntry.input1 = $recipeJson.input1.item
                    }
                    if ($recipeJson.input2) {
                        $recipeEntry.input2 = $recipeJson.input2.item
                    }
                    if ($recipeJson.output) {
                        if ($recipeJson.output -is [string]) {
                            $recipeEntry.output = $recipeJson.output
                        } else {
                            $recipeEntry.output = $recipeJson.output.item
                            if ($recipeJson.output.count) {
                                $recipeEntry.outputCount = $recipeJson.output.count
                            }
                        }
                    }
                    if ($recipeJson.result) {
                        if ($recipeJson.result -is [string]) {
                            $recipeEntry.output = $recipeJson.result
                        } else {
                            $recipeEntry.output = $recipeJson.result.item
                        }
                    }
                    if ($recipeJson.use_item) {
                        $recipeEntry.useItem = $recipeJson.use_item.item
                    }
                    if ($recipeJson.use_on) {
                        $recipeEntry.useOn = $recipeJson.use_on.item
                    }
                    if ($recipeJson.use_description) {
                        $recipeEntry.description = $recipeJson.use_description
                    }
                    # Shaped recipe ingredients
                    if ($recipeJson.key) {
                        $ingredients = [ordered]@{}
                        $recipeJson.key.PSObject.Properties | ForEach-Object {
                            $ingredients[$_.Name] = $_.Value.item
                        }
                        $recipeEntry.ingredients = $ingredients
                        $recipeEntry.pattern = $recipeJson.pattern
                        $recipeEntry.recipeWidth = if ($recipeJson.pattern) { $recipeJson.pattern[0].Length } else { 3 }
                        $recipeEntry.recipeHeight = if ($recipeJson.pattern) { $recipeJson.pattern.Count } else { 3 }
                    }
                    # Module recipe ingredients (list)
                    if ($recipeJson.ingredients) {
                        $ingList = @()
                        foreach ($ing in $recipeJson.ingredients) {
                            if ($ing.item) { $ingList += $ing.item }
                            elseif ($ing.tag) { $ingList += "#$($ing.tag)" }
                        }
                        $recipeEntry.ingredientList = $ingList
                    }

                    $recipes += $recipeEntry
                } catch {
                    Write-Host "  WARN: Failed to parse $($rf.Name)" -ForegroundColor DarkYellow
                }
            }
        }
    }

    # Build category entry
    $words = $fieldName.ToLower().Split('_')
    $label = ($words | ForEach-Object { $_.Substring(0,1).ToUpper() + $_.Substring(1) }) -join ' '

    $catEntry = [ordered]@{
        id = $fieldName
        label = $label
        recipeType = $recipeTypeLower
        categoryClass = $categoryClass
        categoryPath = $catRelPath
        width = $width
        height = $height
        xOffset = $xOff
        yOffset = $yOff
        elements = $elements
        recipes = $recipes
    }

    if ($bgTexturePath) { $catEntry.bgTexture = $bgTexturePath }
    if ($catTextures -and $catTextures.Count -gt 0) { $catEntry.textures = $catTextures }

    $categories += $catEntry
    $texCount = if ($catTextures) { $catTextures.Count } else { 0 }
    Write-Host "  ${categoryClass}: $($elements.Count) elements, $($recipes.Count) recipes, $texCount textures" -ForegroundColor Green
}

# Build and write JSON
$registry = [ordered]@{
    widgetSprites = $widgetSprites
    availableTextures = $availableTextures
    categories = $categories
}
$json = $registry | ConvertTo-Json -Depth 8
$json = $json -replace '\\/', '/'
[System.IO.File]::WriteAllText($outputFile, $json, (New-Object System.Text.UTF8Encoding $false))

Write-Host ""
Write-Host "Generated: $outputFile" -ForegroundColor Cyan
Write-Host "Total categories: $($categories.Count)" -ForegroundColor Cyan
