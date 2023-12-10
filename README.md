This app reads your given league stats and calculates the Roto (Rotisserie) score. It provides the strength of each team's batting, pitching, and overall score and sorts the teams by each category for easy viewing. 

The service comes equipped with an H2 database so that it will work out of the box but there are extra features available if you choose to include a persistent database [Set up DB](#database-set-up). The standard Roto will display the change in each category for each team since the previous calculation. You can also select the 'recent' option to calculate your Roto score for a more granular set of time, with the default being the past month. 

### Background
Most people who play fantasy baseball play Head To Head as it is generally considered more fun. However, it also introduces a large luck factor. The Win Loss records typically do not mirror the strength of each team whereas Roto does. This application will let you see the actual strength of each team and can also be a useful tool in optimizing your roster construction.
# How to Run

This application currently supports the 3 League settings. See below if you play in an unconventional league

#### Step 1.
Find your League. Most likely, you play in the **Standard** League with hitting stats ```R, HR, RBI, SB, AVG``` and pitching `W, SV, K, ERA, WHIP`. 

The **OBP** league are the same categories except with OBP substituted for AVG. 

The **Champions** league has the Standard League categories plus `OPS` and `QS`

[Include Custom League](#Include-Custom-Leagues)
#### Step 2.
Copy your stats into the ```stats.xlsx``` file under the path ```src/main/resources/files/```. Pick the Sheet that matches your league


#### (OPTIONAL) Step 3. 
The default behavior is to generate the Roto score for the Standard league. If you are in a different league then go to `application.properties` file and change the value of **run.league=**. 

You may also change the action **run.action=** to `roto`, `recent`, `only recent`, `change`, `rerun`, or `delete`

See below for more information on these actions


#### Step 4.
Run the Application. Your league's Roto scores will be calculated and saved in an Excel file named after your league under `src/main/resources/files/results` 


# Include Custom Leagues
If you are in a Custom League not supported by this application, you may add it yourself. Go to `League.java` and overwrite one of the leagues with your own settings. Enter the number of players in your league, the number of stat columns, and the number of counting aka cumulative stats (as opposed to averages such as BA, OPS, ERA, WHIP, etc)
Then **Ctrl + Click** on the chosen league's Stats class. Then modify the fields of that class to reflect the stats used in your league. Then, at the bottom of the file, under gatherStats method, include all of the fields. 

# Database Set Up
If you want to calculate the change in scores from previous runs or calculate the Roto score for specific parts of the season, then you will need to install a MySQL database
go the application.properties file and delete the last 5 lines (which specify the temporary H2 database). Replace with the following properties (with your own username and password).
`
    spring.datasource.url=jdbc:mysql://localhost:3306/roto
    spring.datasource.username=username
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
`

# Available Actions
To choose an action, open the application.properties file and enter one of the following values under `run.action`. If left blank, it will perform standard roto
### roto 

Runs regular roto

### recent

If you have a local database installed, then the **recent** option lets you calculate the Roto score for a customizable window of time. You can set the value of **run.weeks** or else it will default to 4. 

Note: this will calculate the regular roto first and then the recent roto. If you have already run this week's roto then choose the next option

### only recent

Generates recent Roto from previously entered values. No new stats are saved to the DB

### change

If 1 player in your league changes his name, then the app will automatically match the player to his prior stats. However, if you have multiple players change names, then select `change` and input the new name and old name in the fields below (**run.oldName** and **run.newName**)

### delete
will delete last week's stats

### rerun
will delete last week's stats and then generate regular roto