# TEST
1. updateName method
2. list mapping

## other
1. league settings
give parameters for stats boundaries
2. sort raw stats by name for better mapping
3. better encapsulate parts into pojos
   4. probably better to replace double map<string, list<double>> with pojo
   5. maybe have one pojo that contains List<Stats> along with the 2 maps
6. make Change its own pojo to be written in Excel 

save later to avoid duplicates in case of error

[//]: # (test withRank)

[//]: # (1. rework roto service to use Stats// )

[//]: # (   2. should use stats the whole time and then map to roto at the end? easier?)

[//]: # (2. save stats after roto calculated)
[//]: # (3. better way to get week)
4. audit columns? include year?
5. testing
   6. use h2 db
   7. integration test- use bdd?

maybe change it to use api calls with league and week as params
or have 2 different calls for 2 different excel files with both leagues
[//]: # (save stats in db tables)

[//]: # (change doubles to floats)

[//]: # (find different way of finding old unmatched roto)


[//]: # (more testing around ties, possibly refactor)



[//]: # ([//]: # &#40;1. either use simplereader for table or &#41; // using db)

[//]: # (2. get rid of a. weekly change or b. secondary categoryRanks)

change statsService to statService and/or split and combine changeService with Roto
