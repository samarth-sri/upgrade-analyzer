# Define the output file to store the consolidated results
$outputFile = "jdeps-results.txt"
# Clear the output file if it already exists
if (Test-Path $outputFile) {
    Remove-Item $outputFile
}
# Get all directories named "classes" under the "target" directories recursively
Get-ChildItem -Recurse -Directory -Filter "classes" | ForEach-Object {
    $classesDir = $_.FullName
    Write-Host "Running jdeps on $classesDir"
    # Run jdeps and capture the output
    $jdepsOutput = & jdeps --recursive "$classesDir" 2>&1
    # Append the output to the consolidated result file
    Add-Content -Path $outputFile -Value "Results for $classesDir`r`n"
    Add-Content -Path $outputFile -Value $jdepsOutput
    Add-Content -Path $outputFile -Value "`r`n"  # Add a blank line between results
}
Write-Host "jdeps results have been compiled into $outputFile"