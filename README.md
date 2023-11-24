This application will convert your league stats into a Rotisserie (Roto) score. It calculates the strength of each team's batting and their pitching and everyone's overall score. It also calculates the change in the Roto score from the last calculation. The teams are sorted by Overall score. They are also sorted by hitting strength and pitching strength in the two tables below. 

Additionally, if you regularly use this app, then you can also calculate the Roto score for a custom amount of time. 

### Background
Most people who play fantasy baseball play Head To Head as it is generally considered more fun. However, it also introduces a large luck factor. The Win Loss records typically do not mirror the strength of each team whereas Roto does. This application will let you see the actual strength of each team and can also be a useful tool in roster construction as it breaks down your score into batting and hitting.
## How to Run 
It currently requires MySQL to be installed locally

This application currently supports the 3 standard League settings. See below if you play in an unconventional league

#### Step 1.
Find your League. Most likely, you play in the **Standard** League with hitting stats ```R, HR, RBI, SB, AVG``` and pitching `W, SV, K, ERA, WHIP`

See below if you play in an unconventional league

#### Step 2.
Copy your stats into the ```stats.xlsx``` file under the path ```src/main/resources/files/```. Pick the Sheet that matches your league

#### (OPTIONAL) Step 3. 
The default behavior is to generate the Roto score for the Standard league. If you are in a different league then go to `application.properties` file and change the value of `run.league=`. 

You may also change the action `run.action=` to `everything`, `recent`, `change`, `rerun`, or `delete`

If you have been running the app on a weekly basis then the `recent` options lets you calculate the Roto score for a smaller window of time with the default being the past 4 weeks. Or change the value of `run.weeks` 

If 1 player in your league changes his name, then the app will automatically match the player to his prior stats. However, if you have multiple players change names, then select the action of `change` and input the new name and old name

#### Step 4.
Run the Application. Your league's Roto scores will be calculated and saved in an excel file with the same name as your league under `src/main/resources/files/` 


## For Custom Leagues
If you are in a Custom League not supported by this application, you may add it yourself. Go to `League.java` and overwrite one of the leagues with your own settings. Enter the number of players in your league, the number of stat columns, and the number of counting aka cumulative stats (as opposed to averages such as BA, OPS, ERA, WHIP, etc)
Then **Ctrl + Click** on the chosen league's Stats class. Then modify that class to include each of the stats used in your league. 


//todo make DB not required