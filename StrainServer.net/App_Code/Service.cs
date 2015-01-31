using System;
using System.Collections;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Xml.Serialization;
using System.Web.Services.Protocols;
using System.Data.Odbc;
using System.Data;

[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
// [System.Web.Script.Services.ScriptService]

public class Service : System.Web.Services.WebService
{
    public Service () {
        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
    }

    private playerNotificationsList listPlayerNotifications = new playerNotificationsList();

    [WebMethod]
    public string HelloWorld() {
        return "test";
    }

    //Other Methods
    private int healthFormula(int baseHealth, int sta)
    {
        return baseHealth + sta * 10;
    }

    private int calculateMeleeAttackDamage(int stat, int[] percentBonus, int[] flatbonus)
    {
        int baseDamage = stat*5;
        int combinedPercentageDamage = 0;
        int combinedFlatDamage = 0;

        if(percentBonus != null)
        {
            for(int i = 0; i < percentBonus.GetLength(0); i++)
            {
                combinedPercentageDamage = combinedPercentageDamage + percentBonus[i];
            }
            combinedPercentageDamage = baseDamage*combinedPercentageDamage/100;
        }

        if (flatbonus != null)
        {
            for (int i = 0; i < flatbonus.GetLength(0); i++)
            {
                combinedFlatDamage = combinedFlatDamage + flatbonus[i];
            }
        }

        return baseDamage + combinedPercentageDamage + combinedFlatDamage;
    }

    private int calculateRangedAttackDamage(int stat, int[] percentBonus, int[] flatbonus)
    {
        int baseDamage = stat * 3;
        int combinedPercentageDamage = 0;
        int combinedFlatDamage = 0;

        if (percentBonus != null)
        {
            for (int i = 0; i < percentBonus.GetLength(0); i++)
            {
                combinedPercentageDamage = combinedPercentageDamage + percentBonus[i];
            }
            combinedPercentageDamage = baseDamage * combinedPercentageDamage / 100;
        }

        if (flatbonus != null)
        {
            for (int i = 0; i < flatbonus.GetLength(0); i++)
            {
                combinedFlatDamage = combinedFlatDamage + flatbonus[i];
            }
        }

        return baseDamage + combinedPercentageDamage + combinedFlatDamage;
    }

    private int calculateDefense(int sta, int per, int[] percentBonus, int[] flatbonus)
    {
        int baseDefense = sta*4/3 + per*2/3;
        int combinedPercentageDefense = 0;
        int combinedFlatDefense = 0;

        if (percentBonus != null)
        {
            for (int i = 0; i < percentBonus.GetLength(0); i++)
            {
                combinedPercentageDefense = combinedPercentageDefense + percentBonus[i];
            }
            combinedPercentageDefense = baseDefense * combinedPercentageDefense / 100;
        }

        if (flatbonus != null)
        {
            for (int i = 0; i < flatbonus.GetLength(0); i++)
            {
                combinedFlatDefense = combinedFlatDefense + flatbonus[i];
            }
        }

        return baseDefense + combinedPercentageDefense + combinedFlatDefense;
    }

    private int calculateRange(int acc, int per, int[] percentBonus, int[] flatbonus)
    {
        int baseRange = acc*8 + per*10;
        int combinedPercentageRange = 0;
        int combinedFlatRange = 0;

        if (percentBonus != null)
        {
            for (int i = 0; i < percentBonus.GetLength(0); i++)
            {
                combinedPercentageRange = combinedPercentageRange + percentBonus[i];
            }
            combinedPercentageRange = baseRange * combinedPercentageRange / 100;
        }

        if (flatbonus != null)
        {
            for (int i = 0; i < flatbonus.GetLength(0); i++)
            {
                combinedFlatRange = combinedFlatRange + flatbonus[i];
            }
        }

        return baseRange + combinedPercentageRange + combinedFlatRange;
    }

    // Read WebMethods

        // - User
    [WebMethod]
    public Boolean isRegistered(String email)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "SELECT username from user WHERE email='" + email.ToLower() + "';";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();
            bool check = myData.Read();

            sqlConn.Close();

            if (check)
                return true;
            else
                return false;
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e);
        }
    }

    [WebMethod]
    public String authenticate(String email, String password)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "SELECT username from user WHERE email='" + email.ToLower() + "' and password='" + password + "';";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();
            bool check = myData.Read();


            sqlConn.Close();

            if (check)
                return "true";
            else
                return "false";
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e);
        }
    }

    [WebMethod]
    public String getUserIdByUsername(String username)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM user WHERE username = ?;";
        sqlCmd.Connection = sqlConn;
        sqlCmd.Parameters.Add("@Username", OdbcType.VarChar, 20);
        sqlCmd.Parameters["@Username"].Value = username;

        sqlConn.Open();


        Object data = sqlCmd.ExecuteScalar();

        sqlConn.Close();


        if (data == null)
            return "";
        else
            return data.ToString();
    }

    [WebMethod]
    public String getActiveCharacterIdByUsername(String username)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM player WHERE user_id ='" + getUserIdByUsername(username.ToLower()) + "' and active_char = 'yes';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String playerid = sqlCmd.ExecuteScalar().ToString();


        sqlConn.Close();

        return playerid;
    }

    [WebMethod]
    public String getInactiveCharacterIdByUsername(String username)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM player WHERE user_id ='" + getUserIdByUsername(username) + "' and active_char = 'no';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String playerid = sqlCmd.ExecuteScalar().ToString();


        sqlConn.Close();

        return playerid;
    }

    [WebMethod]
    public Boolean isPlayerOnline(String username)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT online FROM player WHERE id ='" + getActiveCharacterIdByUsername(username.ToLower()).ToString() + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String status = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        if (status.Equals("yes"))
            return true;

        return false;
    }

    [WebMethod]
    public String getEmailByUsername(String username)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT email FROM user WHERE username ='" + username.ToLower() + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String email = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return email;
    }

    [WebMethod]
    public String getUsernameByEmail(String email)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT username FROM user WHERE email ='" + email.ToLower() + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String username = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return username;
    }

    [WebMethod]
    public String getHumanPlayerIdByUsername(String username)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM player WHERE user_id ='" + getUserIdByUsername(username) + "' and faction_id = 'fact_hum';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String player_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return player_id;
    }

    [WebMethod]
    public String getInfectedPlayerIdByUsername(String username)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM player WHERE user_id ='" + getUserIdByUsername(username) + "' and faction_id = 'fact_inf';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String player_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return player_id;
    }

    [WebMethod]
    public String getUsernameByUserId(String user_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT username FROM user WHERE id ='" + user_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String username = sqlCmd.ExecuteScalar().ToString();


        sqlConn.Close();

        return username;
    }


        // - Player
    [WebMethod]
    public String getPlayerFactionById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT faction_id FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String faction = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return faction;
    }

    [WebMethod]
    public String getPlayerFactionNameById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT faction_name FROM faction WHERE id ='" + getPlayerFactionById(player_id) + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String faction = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return faction;
    }

    [WebMethod]
    public String getPlayerPositionById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT position FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String position = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return position;
    }

    [WebMethod]
    public String getPlayerClassById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT class_id FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String class_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return class_id;
    }

    [WebMethod]
    public String getPlayerClassNameById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT class_name FROM char_class WHERE id ='" + getPlayerClassById(player_id) + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String class_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return class_id;
    }

    [WebMethod]
    public ArrayList getPlayerStatsById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT stat_str, stat_acc, stat_sta, stat_per FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        ArrayList userList = new ArrayList();
        OdbcDataReader myData = sqlCmd.ExecuteReader();

        if (myData.Read())
        {
            userList.Add(myData.GetValue(0).ToString()); //STR
            userList.Add(myData.GetValue(1).ToString()); //ACC
            userList.Add(myData.GetValue(2).ToString()); //STA
            userList.Add(myData.GetValue(3).ToString()); //PER
        }

        sqlConn.Close();

        return userList;
    }

    [WebMethod]
    public String getPlayerStrById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT stat_str FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String stat = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return stat;
    }

    [WebMethod]
    public String getPlayerAccById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT stat_acc FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String stat = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return stat;
    }

    [WebMethod]
    public String getPlayerStaById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT stat_sta FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String stat = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return stat;
    }

    [WebMethod]
    public String getPlayerPerById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT stat_per FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String stat = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return stat;
    }

    [WebMethod]
    public String getPlayerHealthById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT health FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String stat = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return stat;
    }

    [WebMethod]
    public String getPlayerMaxHealthById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT maxhealth FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String stat = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return stat;
    }

    [WebMethod]
    public String getPlayerMoneyById(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT money FROM player WHERE id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String stat = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return stat;
    }
    
    [WebMethod]
    public String getUserIdByPlayerID(String pl_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT user_id FROM player WHERE id = '" + pl_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String user_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return user_id;
    }

    [WebMethod]
    public String getUsernameByPlayerID(String pl_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT user_id FROM player WHERE id = '" + pl_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String user_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return getUsernameByUserId(user_id);
    }

    [WebMethod]
    public ArrayList getPlayerStorageList(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM storage WHERE player_id ='" + player_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        ArrayList storageList = new ArrayList();
        OdbcDataReader myData = sqlCmd.ExecuteReader();

        while (myData.Read())
        {
            storageList.Add(myData.GetValue(0).ToString());
        }

        sqlConn.Close();

        return storageList;
    }

    [WebMethod]
    public String getPlayerInventory(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM storage WHERE player_id ='" + player_id + "' and type='inventory';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String storage_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return storage_id;
    }

    [WebMethod]
    public String getPlayerMailbox(String player_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM storage WHERE player_id ='" + player_id + "' and type='mailbox';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String storage_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return storage_id;
    }


        // - User Score
    [WebMethod]
    public String getUserBestInfectedScoreById(String user_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT best_infected_score FROM score WHERE id ='" + user_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String score = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return score;
    }

    [WebMethod]
    public String getUserBestHumanScoreById(String user_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT best_human_score FROM score WHERE id ='" + user_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String score = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return score;
    }

    [WebMethod]
    public String getUserCurrentScoreById(String user_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT current_score FROM score WHERE id ='" + user_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String score = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return score;
    }

    [WebMethod]
    public String getUserInfectedDeathsById(String user_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT most_deaths_infected FROM score WHERE id ='" + user_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String deaths = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return deaths;
    }

    [WebMethod]
    public String getUserHumanDeathsById(String user_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT most_deaths_human FROM score WHERE id ='" + user_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String deaths = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return deaths;
    }


        // - Faction
    [WebMethod]
    public String getFactionNameById(String faction_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT faction_name FROM faction WHERE id ='" + faction_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String name = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return name;
    }

    [WebMethod]
    public String getFactionIdByName(String faction_name)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM faction WHERE faction_name ='" + faction_name + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return id;
    }

    [WebMethod]
    public String getFactionDescriptionById(String faction_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT faction_description FROM faction WHERE id ='" + faction_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String description = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return description;
    }

    [WebMethod]
    public String getFactionCurrentScoreById(String faction_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT total_score FROM faction WHERE id ='" + faction_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String score = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return score;
    }

    [WebMethod]
    public String getFactionBestScoreById(String faction_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT best_score FROM faction WHERE id ='" + faction_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String score = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return score;
    }


        // - Class
    [WebMethod]
    public String getClassNameById(String class_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT class_name FROM char_class WHERE id ='" + class_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String name = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return name;
    }

    [WebMethod]
    public String getClassIdByName(String class_name)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM char_class WHERE class_name ='" + class_name + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return id;
    }

    [WebMethod]
    public ArrayList getClassBaseStatsById(String class_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT base_stat_str, base_stat_acc, base_stat_sta, base_stat_per FROM char_class WHERE id ='" + class_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        ArrayList statList = new ArrayList();
        OdbcDataReader myData = sqlCmd.ExecuteReader();

        if (myData.Read())
        {
            statList.Add(myData.GetValue(0).ToString()); //STR
            statList.Add(myData.GetValue(1).ToString()); //ACC
            statList.Add(myData.GetValue(2).ToString()); //STA
            statList.Add(myData.GetValue(3).ToString()); //PER
        }

        sqlConn.Close();

        return statList;
    }

    [WebMethod]
    public String getClassAllowedArmorById(String class_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT allow_armor_types FROM char_class WHERE id ='" + class_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String armor = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return armor;
    }

    [WebMethod]
    public String getClassAllowedWeaponTypesById(String class_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT allow_weapon_types FROM char_class WHERE id ='" + class_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String weapons = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return weapons;
    }

    [WebMethod]
    public String getClassBonusById(String class_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT bonus FROM char_class WHERE id ='" + class_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String bonus = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return bonus;
    }

    [WebMethod]
    public String getClassBaseHealthById(String class_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT basehealth FROM char_class WHERE id ='" + class_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String bonus = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return bonus;
    }


        // - Item
    [WebMethod]
    public String getItemNameById(String item_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT item_name FROM item WHERE id ='" + item_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String name = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return name;
    }

    [WebMethod]
    public String getItemTypeById(String item_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT item_type_id FROM item WHERE id ='" + item_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String type = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return type;
    }

    [WebMethod]
    public String getItemTypeNameById(String item_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT type_name FROM item_type WHERE id ='" + getItemTypeById(item_id) + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String typename = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return typename;
    }

    [WebMethod]
    public String getItemSizeById(String item_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT size FROM item WHERE id ='" + item_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String size = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return size;
    }

    [WebMethod]
    public ArrayList getAllEffectsFromItemById(String item_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM item_effects WHERE item_id ='" + item_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        ArrayList effectIdList = new ArrayList();
        OdbcDataReader myData = sqlCmd.ExecuteReader();

        while (myData.Read())
        {
            effectIdList.Add(myData.GetValue(0).ToString());
        }

        sqlConn.Close();

        return effectIdList;
    }


        // - Item Type
    [WebMethod]
    public String getItemTypeTypeNameById(String itemType_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT type_name FROM item_type WHERE id ='" + itemType_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String name = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return name;
    }


        // - Item Effects
    [WebMethod]
    public String getEffectItemById(String effect_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT item_id FROM item_effects WHERE id ='" + effect_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String item = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return item;
    }

    [WebMethod]
    public String getEffectDescriptionById(String effect_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT effect_description FROM item_effects WHERE id ='" + effect_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String description = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return description;
    }

    [WebMethod]
    public String getEffectModifierById(String effect_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT effect FROM item_effects WHERE id ='" + effect_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String effect = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return effect;
    }

    [WebMethod]
    public Boolean isPassiveEffect(String effect_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT is_passive FROM item_effects WHERE id ='" + effect_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String is_passive = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        if (is_passive.Equals("yes"))
            return true;

        return false;
    }

    
        // - Item Instances
    [WebMethod]
    public String getItemInstanceOwnerById(String itemInstance_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT owner_id FROM item_instances WHERE id ='" + itemInstance_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String owner = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return owner;
    }

    [WebMethod]
    public String getItemInstanceItemById(String itemInstance_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT item_id FROM item_instances WHERE id ='" + itemInstance_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String item = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return item;
    }

    [WebMethod]
    public String getItemInstanceItemNameById(String itemInstance_id)
    {
        return getItemNameById(getItemInstanceItemById(itemInstance_id));
    }


    [WebMethod]
    public String getItemInstanceStorageById(String itemInstance_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT storage_id FROM item_instances WHERE id ='" + itemInstance_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String storage = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return storage;
    }

    [WebMethod]
    public String getItemInstanceFlavorById(String itemInstance_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT flavor FROM item_instances WHERE id ='" + itemInstance_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String flavor = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return flavor;
    }

        // - Storage
    [WebMethod]
    public String getStorageOwnerById(String storage_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT player_id FROM storage WHERE id ='" + storage_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String player_id = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return player_id;
    }

    [WebMethod]
    public String getStorageTypeNameById(String storage_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT type FROM storage WHERE id ='" + storage_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String type = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return type;
    }

    [WebMethod]
    public String getStorageLocationById(String storage_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT location FROM storage WHERE id ='" + storage_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String location = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return location;
    }

    [WebMethod]
    public String getStorageMaxCapacityById(String storage_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT capacity FROM storage WHERE id ='" + storage_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String capacity = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return capacity;
    }

    [WebMethod]
    public String getStorageFillById(String storage_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT fill FROM storage WHERE id ='" + storage_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String current_fill = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        return current_fill;
    }

    [WebMethod]
    public Boolean isMobileStorage(String storage_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT is_mobile FROM storage WHERE id ='" + storage_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        String mobile = sqlCmd.ExecuteScalar().ToString();

        sqlConn.Close();

        if (mobile.Equals("yes"))
            return true;

        return false;
    }

    [WebMethod]
    public ArrayList getAllStoredItemInstancesById(String storage_id)
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id FROM item_instances WHERE storage_id ='" + storage_id + "';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        ArrayList itemInstanceList = new ArrayList();
        OdbcDataReader myData = sqlCmd.ExecuteReader();

        while (myData.Read())
        {
            itemInstanceList.Add(myData.GetValue(0).ToString());
        }

        sqlConn.Close();

        return itemInstanceList;
    }

    [WebMethod]
    public Double getDistanceBetween2Points(String p1Lat, String p1Long, String p2Lat, String p2Long)
    {
        Double[] p1 = new Double[2];
        Double[] p2 = new Double[2];

        if (Double.TryParse(p1Lat, out p1[0]) && Double.TryParse(p1Long, out p1[1]) && Double.TryParse(p2Lat, out p2[0]) && Double.TryParse(p2Long, out p2[1]))
        {
            /*double e = (Math.PI * p1[0] / 180);
            double f = (Math.PI * p1[1] / 180);
            double g = (Math.PI * p2[0] / 180);
            double h = (Math.PI * p2[1] / 180);
            double i=(Math.Cos(e)*Math.Cos(g)*Math.Cos(f)*Math.Cos(h)+Math.Cos(e)*Math.Sin(f)*Math.Cos(g)*Math.Sin(h)+Math.Sin(e)*Math.Sin(g));
            double j=(Math.Acos(i));

            return 6371*j;*/

            //return ((p1[0] / 1000000).ToString() + ";" + (p1[1] / 1000000).ToString() + " || " + (p2[0] / 1000000).ToString() + ";" + (p2[1] / 1000000).ToString());

            p1[0] = p1[0] / 1000000 * (Math.PI / 180);
            p1[1] = p1[1] / 1000000 * (Math.PI / 180);
            p2[0] = p2[0] / 1000000 * (Math.PI / 180);
            p2[1] = p2[1] / 1000000 * (Math.PI / 180);

            Double dLat = p2[0] - p1[0];
            Double dLong = p2[1] - p1[1];
            Double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) + Math.Sin(dLong / 2) * Math.Sin(dLong / 2) * Math.Cos(p1[0]) * Math.Cos(p2[0]);
            Double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a)); 
            return 6371 * c * 1000;

            /*Double dLat = p2[0] - p1[0];
            Double dLong = p2[1] - p1[1];
            return Math.Sqrt(Math.Pow(dLat / 100000000000000, 2) + Math.Pow(dLong / 100000000000000, 2));*/
        }

        return -1;
    }

    [WebMethod]
    public ArrayList getOnlineHumanPlayerLocationList()
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id, position FROM player where faction_id = 'fact_hum' and online = 'yes';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        ArrayList positionList = new ArrayList();
        OdbcDataReader myData = sqlCmd.ExecuteReader();

        while (myData.Read())
        {
            positionList.Add(myData.GetValue(0).ToString() + "|" + myData.GetValue(1).ToString());
        }

        sqlConn.Close();

        return positionList;
    }

    [WebMethod]
    public ArrayList getOnlineInfectedPlayerLocationList()
    {
        OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
        OdbcCommand sqlCmd = new OdbcCommand();

        sqlCmd.CommandText = "SELECT id, position FROM player where faction_id = 'fact_inf' and online = 'yes';";
        sqlCmd.Connection = sqlConn;
        sqlConn.Open();

        ArrayList positionList = new ArrayList();
        OdbcDataReader myData = sqlCmd.ExecuteReader();

        while (myData.Read())
        {
            positionList.Add(myData.GetValue(0).ToString() + "|" + myData.GetValue(1).ToString());
        }

        sqlConn.Close();

        return positionList;
    }

    [WebMethod]
    public ArrayList getNearbyHumanPlayersPostion(String coordinates, String baseRadius, String bonus)
    {
        ArrayList allPlayerList = new ArrayList(getOnlineHumanPlayerLocationList());
        ArrayList filteredPlayerList = new ArrayList();
        Double bRadius;
        Double bonusDist;

        Double.TryParse(baseRadius, out bRadius);
        Double.TryParse(bonus, out bonusDist);


        String[] point;

        for (int i = 0; i < allPlayerList.Count; i++)
        {
            point = allPlayerList[i].ToString().Split('|')[1].Split(',');
            if (getDistanceBetween2Points(point[0], point[1], coordinates.Split(',')[0], coordinates.Split(',')[1]) <= bRadius + bonusDist*10 && getDistanceBetween2Points(point[0], point[1], coordinates.Split(',')[0], coordinates.Split(',')[1]) != 0)
            {
                filteredPlayerList.Add(allPlayerList[i]);
            }
        }
        
        return filteredPlayerList;
    }

    [WebMethod]
    public ArrayList getNearbyInfectedPlayersPostion(String coordinates, String baseRadius, String bonus)
    {
        ArrayList allPlayerList = new ArrayList(getOnlineInfectedPlayerLocationList());
        ArrayList filteredPlayerList = new ArrayList();
        Double bRadius;
        Double bonusDist;

        Double.TryParse(baseRadius, out bRadius);
        Double.TryParse(bonus, out bonusDist);


        String[] point;

        for (int i = 0; i < allPlayerList.Count; i++)
        {
            point = allPlayerList[i].ToString().Split('|')[1].Split(',');
            if (getDistanceBetween2Points(point[0], point[1], coordinates.Split(',')[0], coordinates.Split(',')[1]) <= bRadius + bonusDist * 10 && getDistanceBetween2Points(point[0], point[1], coordinates.Split(',')[0], coordinates.Split(',')[1]) != 0)
            {
                filteredPlayerList.Add(allPlayerList[i]);
            }
        }

        return filteredPlayerList;
    }
    //////////



    // Write WebMethods
    [WebMethod]
    public void updateLocation(String player_id, String location) //Location comes like this: (Lat,Long)(0.0000,0.0000)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "UPDATE player set position = '"+ location + "' where id = " + player_id + ";";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();

            
            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }  
    }

    [WebMethod]
    public void updateMaxHealth(String player_id)
    {
        try
        {
            String playerClass = getPlayerClassById(player_id);
            int sta;
            int baseHealth;
            int.TryParse(getPlayerStaById(player_id), out sta);
            int.TryParse(getClassBaseHealthById(playerClass), out baseHealth);
            String sbonuses = getClassBonusById(playerClass);
            int health = healthFormula(baseHealth, sta);

            if (!sbonuses.Equals(""))
            {
                String[] Bonuses = sbonuses.Split(',');
                String[] bonus = null;
                int bonusVal;

                for (int i = 0; i < Bonuses.GetLength(0); i++)
                {
                    bonus = Bonuses[i].Split('_');
                    int.TryParse(bonus[1], out bonusVal);

                    if (bonus[3].Equals("HP"))
                    {
                        if (bonus[2].Equals("%"))
                            health = health + health * bonusVal / 100;
                        else
                            health = health + bonusVal;
                    }
                }
            }

            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "UPDATE player set maxhealth = '" + health + "' where id = " + player_id + ";";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void resetHealth(String player_id)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "UPDATE player set health = '" + getPlayerMaxHealthById(player_id) + "' where id = " + player_id + ";";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void newUser(String username, String email, String password)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText =
                "insert into user (username, email, password) values ('" + username + "', '" + email + "', '" + password + "');";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void newPlayer(String user_id, String faction_id, String position, String class_id, String online, String active)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            ArrayList stats = new ArrayList(getClassBaseStatsById(class_id));

            sqlCmd.CommandText = "insert into player (user_id, faction_id, position, class_id, stat_str, stat_acc, stat_sta, stat_per, money, online, active_char)" +
                " values ('" + user_id + "', '" + faction_id + "', '" + position + "', '" + class_id + "', '" + stats[0] + "', '" + stats[1] + "', '" + stats[2] + "', '" + stats[3] + "', '0', '" + online + "', '" + active + "')";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void makeInventory(String player_id)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "insert into storage (player_id, type, location, capacity, is_mobile, fill)" +
                " values ('" + player_id + "', 'inventory', 'player', '20', 'yes', '0')";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void makeMailbox(String player_id)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "insert into storage (player_id, type, location, capacity, is_mobile, fill)" +
                " values ('" + player_id + "', 'mailbox', 'player', '20', 'yes', '0')";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void makeScoreTable(String user_id)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "insert into score (user_id, best_infected_score, best_human_score, most_deaths_human, most_deaths_infected, current_score)" +
                " values ('" + user_id + "', '0', '0', '0', '0', '0')";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void register(String username, String email, String password, String faction, String playerClass, String position)
    {
        //Create new User
        newUser(username, email, password);

        String uID = getUserIdByUsername(username);

        //Create new score table
        makeScoreTable(uID);

        //Create new character for user
        newPlayer(uID, faction, position, playerClass, "yes", "yes");

        //Set Up character in the other faction for the user
        if(faction.Equals("fact_hum"))
            newPlayer(uID, "fact_inf", "0,0", "i_drone", "no", "no");
        else
            newPlayer(uID, "fact_hum", "0,0", "h_soldier", "no", "no");

        String ap_id = getActiveCharacterIdByUsername(username);
        String ip_id = getInactiveCharacterIdByUsername(username);

        //Create inventory for the Human character
        makeInventory(getActiveCharacterIdByUsername(username));
        makeMailbox(getActiveCharacterIdByUsername(username));
        //Create inventory for the Infected character
        makeInventory(getInactiveCharacterIdByUsername(username));
        makeMailbox(getInactiveCharacterIdByUsername(username));

        //Set up player health
        updateMaxHealth(ap_id);
        updateMaxHealth(ip_id);
        resetHealth(ap_id);
        resetHealth(ip_id);
    }

    [WebMethod]
    public void goOnline(String player_id)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "UPDATE player set online = 'yes' where id = " + player_id + ";";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();

            sqlConn.Close();

            //listPlayerNotifications.addPlayer(getUsernameByPlayerID(player_id));
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void goOffline(String player_id)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = new OdbcCommand();

            sqlCmd.CommandText = "UPDATE player set online = 'no' where id = " + player_id + ";";
            sqlCmd.Connection = sqlConn;
            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }

        //listPlayerNotifications.removePlayer(getUsernameByPlayerID(player_id));
    }

    /*    //Notifications
    [WebMethod]
    public void generateNotificationForUser(String username, String message)
    {
        listPlayerNotifications.getPlayer(username).addNotification(message);
    }

    [WebMethod]
    public ArrayList getAllTheNotificationsForUser(String username)
    {
        return listPlayerNotifications.getPlayer(username).getNotificationList();
    }

    [WebMethod]
    public void dismissNotificationForUser(String username, String message)
    {
        listPlayerNotifications.getPlayer(username).removeNotification(message);
    }

    [WebMethod]
    public void clearNotificationsForUser(String username)
    {
        listPlayerNotifications.getPlayer(username).clearNotifications();
    }

    [WebMethod]
    public ArrayList getUserList()
    {
        return listPlayerNotifications.getAllThePlayers();
    }
      */

        //Combat
    [WebMethod]
    public void strikeMelee(String prey_id, String predator_id)
    {
        try
        {
            int predatorStr;
            int preySta;
            int preyPer;
            int.TryParse(getPlayerStrById(predator_id), out predatorStr);
            int.TryParse(getPlayerStaById(prey_id), out preySta);
            int.TryParse(getPlayerPerById(prey_id), out preyPer);

            int damage = calculateMeleeAttackDamage(predatorStr, null, null) - calculateDefense(preySta, preyPer, null, null);

            if (damage > 0)
            {
                int newHealth;
                int health;
                int.TryParse(getPlayerHealthById(prey_id), out health);

                newHealth = health - damage;

                OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
                OdbcCommand sqlCmd = new OdbcCommand();

                sqlCmd.CommandText = "UPDATE player set health = '" + newHealth + "' where id = " + prey_id + ";";
                sqlCmd.Connection = sqlConn;
                sqlConn.Open();

                OdbcDataReader myData = sqlCmd.ExecuteReader();
                sqlConn.Close();
            }
            else if(damage < 0)
            {
                int newHealth;
                int health;
                int.TryParse(getPlayerHealthById(predator_id), out health);

                newHealth = (health-damage)/2;

                OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
                OdbcCommand sqlCmd = new OdbcCommand();

                sqlCmd.CommandText = "UPDATE player set health = '" + newHealth + "' where id = " + predator_id + ";";
                sqlCmd.Connection = sqlConn;
                sqlConn.Open();

                OdbcDataReader myData = sqlCmd.ExecuteReader();
                sqlConn.Close();
            }
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public String strikeRanged(String prey_id, String predator_id)
    {
        try
        {
            int predatorAcc;
            int preySta;
            int preyPer;
            int.TryParse(getPlayerAccById(predator_id), out predatorAcc);
            int.TryParse(getPlayerStaById(prey_id), out preySta);
            int.TryParse(getPlayerPerById(prey_id), out preyPer);

            int damage = calculateRangedAttackDamage(predatorAcc, null, null) - calculateDefense(preySta, preyPer, null, null);

            if (damage > 0)
            {
                int newHealth;
                int health;
                int.TryParse(getPlayerHealthById(prey_id), out health);

                newHealth = health - damage;

                if (newHealth < 0)
                {
                    newHealth = 0;
                }

                OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
                OdbcCommand sqlCmd = new OdbcCommand();

                sqlCmd.CommandText = "UPDATE player set health = '" + newHealth + "' where id = " + prey_id + ";";
                sqlCmd.Connection = sqlConn;
                sqlConn.Open();

                OdbcDataReader myData = sqlCmd.ExecuteReader();
                sqlConn.Close();

                if (getPlayerHealthById(prey_id).Equals("0"))
                {
                    return "enemy,dead," + damage.ToString();
                }
                else
                {
                    return "enemy,alive," + damage.ToString();
                }

            }
            else if (damage < 0)
            {
                int newHealth;
                int health;
                int.TryParse(getPlayerHealthById(predator_id), out health);

                newHealth = (health - damage) / 2;

                if (newHealth < 0)
                {
                    newHealth = 0;
                }

                OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
                OdbcCommand sqlCmd = new OdbcCommand();

                sqlCmd.CommandText = "UPDATE player set health = '" + newHealth + "' where id = " + predator_id + ";";
                sqlCmd.Connection = sqlConn;
                sqlConn.Open();

                OdbcDataReader myData = sqlCmd.ExecuteReader();
                sqlConn.Close();

                if (getPlayerHealthById(prey_id).Equals("0"))
                {
                    return "user,dead," + damage.ToString();
                }
                else
                {
                    return "user,alive," + damage.ToString();
                }
            }
            else
            {
                return "tie,alive,0";
            }
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public String isInRange(String prey_id, String predator_id)
    {
        int predPer;
        int predAcc;
        int.TryParse(getPlayerPerById(predator_id), out predPer);
        int.TryParse(getPlayerAccById(predator_id), out predAcc);
        int range = calculateRange(predAcc, predPer, null, null);
        
        String[] predPos = getPlayerPositionById(predator_id).Split(',');
        String[] preyPos = getPlayerPositionById(prey_id).Split(',');

        Double distance = getDistanceBetween2Points(predPos[0], predPos[1], preyPos[0], preyPos[1]);

        if (distance <= range)
            //return range.ToString() + ">" + distance;
            return "true";
        else
            //return range.ToString() + "<" + distance; ;
            return "false";
    }
    
        //Item Generation/Deletion
    [WebMethod]
    public void newItem(String item_id, String owner_id, String storage_id, String flavor)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = sqlConn.CreateCommand();

            sqlCmd.CommandText = "insert into item_instances (item_id, owner_id, storage_id, flavor) values ('" + item_id + "', '" + owner_id + "', '" + storage_id + "', ?);";

            sqlCmd.Parameters.Add("@Flavor", OdbcType.VarChar, 256);
            sqlCmd.Parameters["@Flavor"].Value = flavor;

            sqlConn.Open();

            OdbcDataReader myData = sqlCmd.ExecuteReader();


            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

    [WebMethod]
    public void deleteItem(String itemInstance_id, String storage_id)
    {
        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = sqlConn.CreateCommand();

            sqlCmd.CommandText = "DELETE FROM item_instances WHERE id=" + itemInstance_id + ";";

            sqlConn.Open();
            OdbcDataReader myData = sqlCmd.ExecuteReader();
            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }

        try
        {
            OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
            OdbcCommand sqlCmd = sqlConn.CreateCommand();

            int fill;

            int.TryParse(getStorageFillById(storage_id), out fill);

            fill--;

            sqlCmd.CommandText = "update storage set fill=" + fill + " WHERE id=" + storage_id + ";";

            sqlConn.Open();
            OdbcDataReader myData = sqlCmd.ExecuteReader();
            sqlConn.Close();
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }

        //Mail
    [WebMethod]
    public String newMailItem(String sender_id, String recipient_username, String message)
    {
        if (getUserIdByUsername(recipient_username).Equals(""))
        {
            return "error:username";
        }

        String recipient_mailbox = getPlayerMailbox(getHumanPlayerIdByUsername(recipient_username));

        int fill;
        int capacity;

        int.TryParse(getStorageFillById(recipient_mailbox), out fill);
        int.TryParse(getStorageMaxCapacityById(recipient_mailbox), out capacity);

        if (fill + 1 > capacity)
        {
            return "error:full";
        }

        String faction = getPlayerFactionById(sender_id);
        if (faction.Equals("fact_hum"))
        {
            //Create new Item
            newItem("mail_letter", sender_id, recipient_mailbox, message);
        }
        else
        {
            //Create new Item
            newItem("mail_letter", sender_id, recipient_mailbox, message);
        }

        return "ok";
    }

        //Trading
    [WebMethod]
    public String changeStorage(String itemInst_id, String newStorage_id)
    {
        try
        {
            String storage1 = getItemInstanceStorageById(itemInst_id);
            String item = getItemInstanceItemById(itemInst_id);
            int storage2Capacity = 0;
            int newstorage1fill = 0;
            int oldstorage1fill = 0;
            int newstorage2fill = 0;
            int oldstorage2fill = 0;
            int itemSize = 0;

            int.TryParse(getStorageMaxCapacityById(newStorage_id), out storage2Capacity);
            int.TryParse(getStorageFillById(storage1), out oldstorage1fill);
            int.TryParse(getStorageFillById(newStorage_id), out oldstorage2fill);
            int.TryParse(getItemSizeById(item), out itemSize);

            newstorage1fill = oldstorage1fill - itemSize;
            newstorage2fill = oldstorage2fill + itemSize;

            if (newstorage2fill > storage2Capacity)
            {
                return "error:capacity";
            }
            else
            {

                OdbcConnection sqlConn = new OdbcConnection("Driver={MySQL ODBC 5.1 Driver}; Server=localhost; Database=strain_db; Uid=root; Pwd=");
                OdbcCommand sqlCmd = new OdbcCommand();

                sqlCmd.CommandText = "UPDATE storage set fill = " + newstorage1fill + " where id = " + storage1 + ";";
                sqlCmd.Connection = sqlConn;
                sqlConn.Open();

                OdbcDataReader myData = sqlCmd.ExecuteReader();

                sqlConn.Close();

                sqlCmd.CommandText = "UPDATE item_instances set storage_id=" + newStorage_id + " where id = " + itemInst_id + ";";
                sqlCmd.Connection = sqlConn;
                sqlConn.Open();

                myData = sqlCmd.ExecuteReader();

                sqlConn.Close();

                sqlCmd.CommandText = "UPDATE storage set fill = " + newstorage2fill + " where id = " + newStorage_id + ";";
                sqlCmd.Connection = sqlConn;
                sqlConn.Open();

                myData = sqlCmd.ExecuteReader();

                sqlConn.Close();

                return "ok";
            }
        }
        catch (OdbcException o)
        {
            throw new Exception("ODBC Error: " + o);
        }
        catch (Exception e)
        {
            throw new Exception("Error: " + e + ":(");
        }
    }
    
    //////////
}