# GUI Designer HTTP Server
# Serves static files + API endpoints for file read/write and texture listing

$projectRoot = (Resolve-Path "$PSScriptRoot\..\..").Path
$textureBase = "$projectRoot\src\main\resources\assets\lostdepths\textures"
$port = 8090

$listener = New-Object System.Net.HttpListener
$listener.Prefixes.Add("http://localhost:${port}/")
$listener.Start()
Write-Host "GUI Designer Server running on http://localhost:${port}" -ForegroundColor Cyan
Write-Host "Project: $projectRoot" -ForegroundColor DarkGray
Write-Host "Textures: $textureBase" -ForegroundColor DarkGray
Write-Host ""

function Send-Json($ctx, $obj, $status = 200) {
    $json = $obj | ConvertTo-Json -Depth 10 -Compress
    $bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
    $ctx.Response.StatusCode = $status
    $ctx.Response.ContentType = "application/json"
    $ctx.Response.ContentLength64 = $bytes.Length
    $ctx.Response.OutputStream.Write($bytes, 0, $bytes.Length)
}

function Send-Error($ctx, $msg, $status = 500) {
    Send-Json $ctx @{ error = $msg } $status
}

function Read-RequestBody($req) {
    $reader = New-Object System.IO.StreamReader($req.InputStream)
    $body = $reader.ReadToEnd()
    $reader.Close()
    return $body | ConvertFrom-Json
}

while ($listener.IsListening) {
    $ctx = $listener.GetContext()
    $req = $ctx.Request
    $path = $req.Url.LocalPath
    $method = $req.HttpMethod

    try {
        # --- API: Read file ---
        if ($method -eq "POST" -and $path -eq "/api/read-file") {
            $data = Read-RequestBody $req
            $filePath = Join-Path $projectRoot $data.path
            if (-not (Test-Path $filePath)) {
                Send-Error $ctx "File not found: $($data.path)" 404
            } else {
                $content = [System.IO.File]::ReadAllText($filePath, [System.Text.Encoding]::UTF8)
                Send-Json $ctx @{ content = $content }
            }
        }
        # --- API: Write file ---
        elseif ($method -eq "POST" -and $path -eq "/api/save-file") {
            $data = Read-RequestBody $req
            $filePath = Join-Path $projectRoot $data.path
            if (-not (Test-Path $filePath)) {
                Send-Error $ctx "File not found: $($data.path)" 404
            } else {
                [System.IO.File]::WriteAllText($filePath, $data.content, (New-Object System.Text.UTF8Encoding $false))
                Send-Json $ctx @{ ok = $true }
            }
        }
        # --- API: List textures ---
        elseif ($method -eq "GET" -and $path -eq "/api/list-textures") {
            $textures = @()
            $folders = @("screens", "gui")
            foreach ($folder in $folders) {
                $dir = "$textureBase\$folder"
                if (Test-Path $dir) {
                    Get-ChildItem "$dir\*.png" -ErrorAction SilentlyContinue | ForEach-Object {
                        $textures += @{
                            name = $_.BaseName
                            folder = $folder
                            path = "textures/$folder/$($_.Name)"
                            servePath = "/textures/$folder/$($_.Name)"
                        }
                    }
                }
            }
            Send-Json $ctx @{ textures = $textures }
        }
        # --- Serve texture files ---
        elseif ($method -eq "GET" -and $path -match "^/textures/") {
            $relPath = $path.Substring(1) # remove leading /
            $filePath = Join-Path $textureBase ($relPath.Replace("textures/", ""))
            if (Test-Path $filePath) {
                $bytes = [System.IO.File]::ReadAllBytes($filePath)
                $ctx.Response.ContentType = "image/png"
                $ctx.Response.ContentLength64 = $bytes.Length
                $ctx.Response.OutputStream.Write($bytes, 0, $bytes.Length)
            } else {
                $ctx.Response.StatusCode = 404
            }
        }
        # --- Static files ---
        else {
            if ($path -eq "/") { $path = "/index.html" }
            $file = Get-Item -LiteralPath ($PSScriptRoot + $path) -ErrorAction SilentlyContinue
            if ($file) {
                $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
                $ext = $file.Extension
                $ct = switch ($ext) {
                    ".html" { "text/html" }
                    ".json" { "application/json" }
                    ".js"   { "application/javascript" }
                    ".css"  { "text/css" }
                    ".png"  { "image/png" }
                    default { "application/octet-stream" }
                }
                $ctx.Response.ContentType = $ct
                $ctx.Response.ContentLength64 = $bytes.Length
                $ctx.Response.OutputStream.Write($bytes, 0, $bytes.Length)
            } else {
                $ctx.Response.StatusCode = 404
            }
        }
    } catch {
        try { Send-Error $ctx $_.Exception.Message } catch {}
    }

    $ctx.Response.Close()
}
