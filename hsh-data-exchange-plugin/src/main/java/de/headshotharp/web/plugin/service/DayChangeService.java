package de.headshotharp.web.plugin.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import de.headshotharp.web.database.User;
import de.headshotharp.web.database.UserValueHistory;
import de.headshotharp.web.plugin.hibernate.DataProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DayChangeService {

    private DataProvider dp;

    public int saveUserHistory(Session session) {
        List<User> users = dp.user(session).findAll();
        for (User user : users) {
            dp.userValueHistory(session).persist(user.createValueHistory());
        }
        return users.size();
    }

    public void saveUserHistoryConditionally(Session session) {
        if (hasDayChanged(session)) {
            saveUserHistory(session);
        }
    }

    public boolean hasDayChanged(Session session) {
        UserValueHistory blockHistory = dp.userValueHistory(session).findLatest().orElse(null);
        if (blockHistory == null) {
            return true;
        } else {
            return isToday(blockHistory.getCreated());
        }
    }

    /* automatic session wrappers */

    public int saveUserHistory() {
        try (Session session = dp.openTransaction()) {
            int count = saveUserHistory(session);
            dp.commitTransaction(session);
            return count;
        }
    }

    public void saveUserHistoryConditionally() {
        try (Session session = dp.openTransaction()) {
            saveUserHistoryConditionally(session);
            dp.commitTransaction(session);
        }
    }

    public boolean hasDayChanged() {
        try (Session session = dp.openTransaction()) {
            boolean ret = hasDayChanged(session);
            dp.commitTransaction(session);
            return ret;
        }
    }

    /* utilities */

    public boolean isToday(Date date) {
        return isSameDay(date, new Date());
    }

    public boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
