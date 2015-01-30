using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Web;

/// <summary>
/// Summary description for Class1
/// </summary>
public class playerNotificationsList
{
    private List<playerNotifications> playerNotif;

    public playerNotificationsList()
    {
        playerNotif = new List<playerNotifications>();
    }

    public ArrayList getAllThePlayers()
    {
        ArrayList userList = new ArrayList();

        foreach (playerNotifications p in playerNotif)
        {
            userList.Add(p.getUsername());
        }

        return userList;
    }

    //Notification Management
    public void addPlayer(String username)
    {
        playerNotif.Add(new playerNotifications(username));
    }

    public void removePlayer(String username)
    {
        foreach (playerNotifications p in playerNotif)
        {
            if (p.getUsername() == username)
            {
                playerNotif.Remove(p);
                return;
            }
        }
    }

    public ArrayList getPlayerNotifications(String username)
    {
        foreach (playerNotifications p in playerNotif)
        {
            if (p.getUsername() == username)
            {
                return p.getNotificationList();
            }
        }

        return null;
    }

    public playerNotifications getPlayer(String username)
    {
        foreach (playerNotifications p in playerNotif)
        {
            if (p.getUsername() == username)
            {
                return p;
            }
        }

        return null;
    }
}