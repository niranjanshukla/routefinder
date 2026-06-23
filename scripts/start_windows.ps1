param([switch]$Build)

$Image     = "route-finder"
$Container = "route-finder-app"
$Port      = 8080

if ($Build -or -not (docker image inspect $Image 2>$null)) {
    Write-Host "Building image $Image..."
    docker build -t $Image "$PSScriptRoot\.."
}

$running = docker ps -q -f name=$Container
if ($running) {
    Write-Host "Container $Container is already running."
} else {
    docker rm -f $Container 2>$null
    docker run -d --name $Container -p "${Port}:8080" $Image
    Write-Host "Started at http://localhost:$Port"
}
