# Instructions for using UpgradeAnalyzer utility

## UpgradeAnalyzer
### Build using
`mvn clean package`
### Run using
`java -jar ./target/upgrade-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -Dcommand=UpgradeAnalyzer -DcsvPath=<csv_file_path> -DdirToScan=<root_dir> UpgradeAnalyzerMain`

***

## DependencyAggregator
### Build using
`mvn clean package`
### Run using
`java -jar ./target/upgrade-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -Dcommand=DependencyAggregator -DdepFileName=<dependencies_file_name> -DdirToScan=<root_dir> UpgradeAnalyzerMain`