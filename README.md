This app reads your given league stats and calculates the Roto (Rotisserie) score. It provides the strength of each team's batting, pitching, and overall score and sorts the teams by each category for easy viewing. 


### Background
Most people who play fantasy baseball play Head To Head as it is generally considered more fun. However, it also introduces a large luck factor. The Win Loss records typically do not mirror the strength of each team whereas Roto does. This application will let you see the actual strength of each team and can also be a useful tool in optimizing your roster construction.

# How to Run


#### Step 1.
Find your League. Most likely, you play in the **Standard** League with hitting stats ```R, HR, RBI, SB, AVG``` and pitching `W, SV, K, ERA, WHIP`. If not then see
[League Information](#Leagues)

#### Step 2.
Copy your stats, into the ```input-stats.xlsx``` file. Pick the Sheet that matches your league. Follow the format displayed with the header on top labeling each category


#### Step 3. 
The default behavior is to generate the Roto score for the Standard league. If you are in a different league then open the `options.properties` file and change the value of **league=**

If you choose to set up in your database in the next step, then you may also change **action=** from `roto` to `recent`, `only recent`, `change`, `rerun`, or `delete`

See [Available Actions](#Available-Actions) for more information

#### (OPTIONAL) Step 4.
The service comes equipped with an H2 database so that it will work out of the box but there are extra features available if you choose to include a persistent database - [Set up DB](#database-set-up). 

The major feature is that you can select the 'recent' option to calculate your Roto score for a more granular set of time, with the default being the past month.
Also, the standard Roto will display the change in each category for each team since the previous calculation.


#### Final Step
Run the Application. Your league's Roto scores will be calculated and saved in an Excel file named `results.xlsx` 


## Database Set Up
If you want to calculate the change in scores from previous runs or calculate the Roto score for specific parts of the season, then you will need to install a MySQL database
Navigate to `src/main/resources/files/` and open the `application.properties` file and delete the lines inside (which specify the temporary H2 database). Replace with the following properties (with the username and password to your database).


    spring.datasource.username=username
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/roto



## Leagues
There are a number of custom league options already available. If none of them match your settings, then follow the steps [here](#Include-Custom-League) 

These leagues have a default setting of 12 teams per league (14 for CHAMPIONS). You may change this in the League.java class directly or the `numberOfPlayers` variable in the `options.properties` file
#### STANDARD
Hitting Stats - `R, HR, RBI, SB, AVG`

Pitching Stats - `W, SV, K, ERA, WHIP`

#### OBP
Hitting Stats - `R, HR, RBI, SB, OBP`

Pitching Stats - `W, SV, K, ERA, WHIP`

#### CHAMPIONS
Hitting Stats - `R, HR, RBI, SB, AVG, OPS`

Pitching Stats - `W, SV, K, ERA, WHIP, QS`

Default number of teams - 14
#### PSD
Hitting Stats - `R, HR, RBI, SB, OPS`

Pitching Stats - `SV, K, ERA, WHIP, QS`

### Include Custom League
If you are in a Custom League not covered by one of these existing leagues, you may add it yourself by following these steps
1. Go to `src/main/java/com/baseball/roto/model/League.java` and overwrite the `CUSTOM` league with your own settings. Replace the number of players in your league, the number of stat columns, and the number of counting aka cumulative stats (as opposed to averages such as BA, OPS, ERA, WHIP, etc)
2. **Ctrl + Click** on _CustomStats.class_ as a shortcut or go to `src/main/java/com/baseball/roto/model/entity/CustomStats.class`. Modify the fields of that class to reflect the stats used in your league. Averaged stats should use `long` and cumulative `int`. For example `private float avg;` or  `private int homeRuns;` 
3. The line above` @Column(name="HR")` will have an abbreviation that must match the values of the headers in the `stats.xslx` sheet.
4. At the bottom of the `CustomStats.class`, repeat all of your stat changes in the relevant **gatherHittingStats()** or **gatherPitchingStats()** methods. You do this by adding the line `stats.add(YOUR_STAT_NAME);` for averaged stats and for `stats.add((float) YOUR_STAT_NAME);` cumulative stats. and removing any stats you do not have. Any ratio stat (like OPS or ERA) must be listed after the cumulative stats (RBI, K)

Note- Further configuration would be required if you have an unusual stat types such as pitcher ratios which ascend like K/BB rate or cumulative stats which descend.

## Available Actions
To choose an action, open the `options.properties` file and enter one of the following values under `action`. If left blank, it will perform standard roto. The other options are only available if you have set up your database.
#### roto 

Runs regular roto

#### recent

If you have a local database installed, then the **recent** option lets you calculate the Roto score for a customizable window of time. You can set the value of **weeks** or else it will default to 4. 

Note: this will calculate the regular roto first and then the recent roto. If you have already run this week's roto then choose the next option

#### only recent

Generates recent Roto from previously entered values. No new stats are saved to the DB so choose this option if you have already calculated the current week.

#### change

If 1 player in your league changes his name, then the app will automatically match the player to his prior stats. However, if you have multiple players change names, then select `change` and input the new name and old name in the fields below (**oldName** and **newName**)

#### delete
will delete last week's stats

#### rerun
will delete last week's stats and then generate regular roto