# Define the JDK release version to scan for deprecated APIs
$releaseVersion = 17  # Change this to the desired JDK version
# Define the output file to store the consolidated results
$outputFile = "jdeprscan-17-results.txt"
# Clear the output file if it already exists
if (Test-Path $outputFile) {
    Remove-Item $outputFile
}
# Get all directories named "classes" under the "target" directories recursively
Get-ChildItem -Recurse -Directory -Filter "classes" | ForEach-Object {
    $classesDir = $_.FullName
    Write-Host "Running jdeprscan on $classesDir"
    # Run jdeprscan and capture the output
    $jdeprscanOutput = & jdeprscan --release $releaseVersion "$classesDir" 2>&1
    # Append the output to the consolidated result file
    Add-Content -Path $outputFile -Value "Results for $classesDir`r`n"
    Add-Content -Path $outputFile -Value $jdeprscanOutput
    Add-Content -Path $outputFile -Value "`r`n"  # Add a blank line between results
}

Write-Host "jdeprscan17 results have been compiled into $outputFile"