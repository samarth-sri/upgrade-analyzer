# Instructions for using UpgradeAnalyzer utility

## UpgradeAnalyzer
### Build using:
`mvn clean package`
### Run using:
`java -Dcommand=CodeScanner -DcsvPath=<csv_file_path> -DdirToScan=<root_dir> -jar ./target/upgrade-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar UpgradeAnalyzerMain`

***

## DependencyAggregator
**_Prerequisites:_** Generate dependencies list for the maven(multi-module) project excluding the internal maven modules:

`mvn dependency:list -DexcludeTransitive="true" -DoutputFile="dependencies.txt" -DexcludeGroupIds=<parent-pom-groupId>`

### Build using:
`mvn clean package`

### Run using:
`java -Dcommand=DependencyAggregator -DdepFileName=<dependencies_file_name> -DdirToScan=<root_dir> -jar ./target/upgrade-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar UpgradeAnalyzerMain`