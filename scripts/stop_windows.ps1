$Container = "route-finder-app"
docker stop $Container
docker rm $Container
Write-Host "Stopped and removed $Container"
