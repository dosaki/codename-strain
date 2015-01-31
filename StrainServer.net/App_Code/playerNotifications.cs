using System;
using System.Collections;
using System.Linq;
using System.Web;

/// <summary>
/// This class will serve as an item to keep notifications of a player.
/// </summary>
public class playerNotifications
{
    private String username;
    private ArrayList notifications;

    public playerNotifications()
    {
        this.username = "";
        this.notifications = new ArrayList();
    }

    public playerNotifications(String username)
    {
        this.username = username;
        this.notifications = new ArrayList();
    }

    public String getUsername()
    {
        return this.username;
    }

    public ArrayList getNotificationList()
    {
        return this.notifications;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void addNotification(String notificationText)
    {
        notifications.Add((notifications.Count+1).ToString() + " " + notificationText);
    }

    public void removeNotification(String notificationText)
    {
        notifications.Remove(notificationText);
    }

    public void clearNotifications()
    {
        this.notifications.Clear();
    }
}
