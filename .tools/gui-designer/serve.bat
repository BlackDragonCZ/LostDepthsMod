@echo off
echo Starting GUI Designer on http://localhost:8090
echo Press Ctrl+C to stop
cd /d "%~dp0"
powershell -Command "Start-Process 'http://localhost:8090'"
powershell -Command "$listener = New-Object System.Net.HttpListener; $listener.Prefixes.Add('http://localhost:8090/'); $listener.Start(); Write-Host 'Server running...'; while ($listener.IsListening) { $ctx = $listener.GetContext(); $path = $ctx.Request.Url.LocalPath; if ($path -eq '/') { $path = '/index.html' }; $file = (Get-Item -LiteralPath ($PWD.Path + $path) -ErrorAction SilentlyContinue); if ($file) { $bytes = [System.IO.File]::ReadAllBytes($file.FullName); $ext = $file.Extension; $ct = switch($ext) { '.html' {'text/html'} '.json' {'application/json'} '.js' {'application/javascript'} '.css' {'text/css'} '.png' {'image/png'} default {'application/octet-stream'} }; $ctx.Response.ContentType = $ct; $ctx.Response.ContentLength64 = $bytes.Length; $ctx.Response.OutputStream.Write($bytes, 0, $bytes.Length) } else { $ctx.Response.StatusCode = 404 }; $ctx.Response.Close() }"
