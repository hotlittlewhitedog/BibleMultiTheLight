
package org.hlwd.bible;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.*;
import java.util.*;

/**
 * Project Common Class
 *
 * Special characters: { } [ ] ||
 */
public final class PCommon implements IProject
{
    //<editor-fold defaultstate="collapsed" desc="-- Variables --">

    //The following variable should be false before putting on the Market and Debuggable=False in manifest

    protected final static boolean _isDebugVersion = true;

    protected final static LayoutParams _layoutParamsWrap = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    protected final static LayoutParams _layoutParamsMatchAndWrap = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    protected final static LayoutParams _layoutParamsMatch = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    private static SCommon _s = null;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="-- Initializer --">

    /***
     * Initializer
     */
    private PCommon()
    {
    }

    //</editor-fold>

    /***
     * Check local instance
     * @param context
     */
    private static void CheckLocalInstance(final Context context)
    {
        try
        {
            if (_s == null) _s = SCommon.GetInstance(context);
        }
        catch(Exception ex)
        {
            LogR(context, ex);
        }
    }

    /**
     * Convert stack trace to string
     * @param stackTrace    Stack trace
     * @return string
     */
    protected static String StackTraceToString(final StackTraceElement[] stackTrace)
    {
        final StringWriter sw = new StringWriter();
        PrintStackTrace(stackTrace, new PrintWriter(sw));

        return sw.toString();
    }

    private static void PrintStackTrace(final StackTraceElement[] stackTrace, final PrintWriter pw)
    {
        for(StackTraceElement stackTraceElement : stackTrace)
        {
            pw.println(stackTraceElement);
        }
    }

    /**
     * Concatenate objects (generic) with StringBuilder
     * @param args  Arguments
     * @return string
     */
    protected static String ConcaT(final Object... args)
    {
        final StringBuilder sb = new StringBuilder();

        for (final Object obj : args)
        {
            if (obj != null) sb.append(obj.toString());
        }

        return sb.toString();
    }

    /***
     * Add quotes at start & Stop of string
     * @param value Field value
     * @return Quotated string
     */
    protected static String AQ(final String value)
    {
        final String newValue = PCommon.ConcaT("'", value, "'");

        return newValue;
    }

    /***
     * Replace quotes (') by double quotes in fields
     * @param value     Field value
     * @return Field value ready to be concatenated in sql query
     */
    protected static String RQ(final String value)
    {
        if (!value.contains("'"))
        {
            return value;
        }

        final String newValue = value.replaceAll("'", "''");

        return newValue;
    }

    /**
     * Get current date time (E.G.: 23/09 14:34:20)
     * @return NowFunc
     */
    protected static String NowFunc()
    {
        return DateFormat.format("dd/MM kk:mm:ss", new Date()).toString();
    }

    /**
     * Get current date YYYYMMDD (E.G.: 20160818)
     * @return YYYYMMDD
     */
    protected static String NowYYYYMMDD()
    {
        return DateFormat.format("yyyyMMdd", new Date()).toString();
    }

    /***
     * Get Now in millics (since Epoch)
     * @return long value
     *
    protected static long NowInMillics()
    {
        return new Date().getTime();
    }
    */

    /**
     * Get current time (E.G.: 14:34:20)
     * @return NowFunc
     */
    protected static String TimeFunc()
    {
        return DateFormat.format("kk:mm:ss", new Date()).toString();
    }

    /**
     * Get current time (E.G.: 143420)
     * @return NowFunc
     */
    protected static String TimeFuncShort()
    {
        final String now = DateFormat.format("kkmmss", new Date()).toString();

        return now;
    }

    /**
     * Save key in SharedPreferences
     * @param context
     * @param key
     * @param value
     */
    protected static void SavePref(final Context context, final APP_PREF_KEY key, final String value)
    {
        //SharedPreferences appPrefs = context.getSharedPreferences("task1", MODE_PRIVATE);
        final SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        final SharedPreferences.Editor editor = appPrefs.edit();
        editor.putString(key.toString(), value);
        editor.commit();
    }

    /**
     * Save key in SharedPreferences
     * @param context
     * @param key   integer logs as String
     * @param value
     */
    protected static void SavePrefInt(final Context context, final APP_PREF_KEY key, final int value) {

        SavePref(context, key, ConcaT(value));

        //LogD(context, key);
    }

    /**
     * Get key from SharedPreferences (defaultValue is "")
     * @param context
     * @param key
     * @return
     */
    protected static String GetPref(final Context context, final APP_PREF_KEY key) {

        return GetPref(context, key, "");
    }

    /**
     * Get key from SharedPreferences
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    protected static String GetPref(final Context context, final APP_PREF_KEY key, final String defaultValue)
    {
        final SharedPreferences appPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        return appPrefs.getString(key.toString(), defaultValue);
    }

    /***
     * Get BIBLE_NAME
     * @param context
     */
    protected static String GetPrefBibleName(final Context context)
    {
        String bbName = PCommon.GetPref(context, APP_PREF_KEY.BIBLE_NAME);
        if (bbName == "" || bbName == null) bbName = "k";

        return bbName;
    }

    /***
     * Get TRAD_BIBLE_NAME
     * @param context
     * @param canReturnDefaultValue  False => its real value (can be empty), True => if empty: fill with a default value
     */
    protected static String GetPrefTradBibleName(final Context context, final boolean canReturnDefaultValue)
    {
        String trad = PCommon.GetPref(context, IProject.APP_PREF_KEY.TRAD_BIBLE_NAME);
        if (trad == "" || trad == null)
        {
            if (canReturnDefaultValue)
            {
                trad = GetPrefBibleName(context);
            }
            else
            {
                trad = "";
            }
        }

        return trad;
    }

    /***
     * Get theme ID
     * @param context
     * @return theme ID
     */
    protected static int GetPrefThemeId(final Context context)
    {
        final String THEME_NAME = PCommon.GetPrefThemeName(context);
        final int themeId;

        switch (THEME_NAME)
        {
            case "LIGHT":
                themeId = R.style.AppThemeLight;
                break;

            case "LIGHT_AND_BLUE":
                themeId = R.style.AppThemeDev;
                break;

            case "DARK":
                themeId = R.style.AppThemeDark;
                break;

            case "KAKI":
                themeId = R.style.AppThemeLight;
                break;

            default:
                themeId = R.style.AppThemeLight;
                break;
        }

        return themeId;
    }

    /**
     * Manage TRAD_BIBLE_NAME (set)
     * @param context
     * @param operation When = 0 then return lang stack, when > 0 then add else remove
     * @param bbName    bbName
     * @return String with selected language names
     */
    protected static String ManageTradBibleName(final Context context, final int operation, final String bbName)
    {
        boolean valueChanged = false;
        String trad = PCommon.GetPrefTradBibleName(context, false);
        if (operation > 0)
        {
            //Add
            if (trad.indexOf(bbName) < 0)
            {
                trad = PCommon.ConcaT(trad, bbName);
                valueChanged = true;
            }
        }
        else if (operation < 0)
        {
            //Remove
            if (trad.indexOf(bbName) >= 0)
            {
                trad = trad.replace(bbName, "");
                valueChanged = true;
            }
        }
        //Save TRAD
        if (valueChanged) PCommon.SavePref(context, APP_PREF_KEY.TRAD_BIBLE_NAME, trad);

        //Returns lang stack
        final int size = trad.length();
        StringBuilder sb = new StringBuilder("");
        String bb;
        for (int i = 0; i < size; i++)
        {
            bb = trad.substring(i, i + 1);
            if (bb.compareToIgnoreCase("k") == 0)
            {
                sb.append(context.getString(R.string.languageEnShort));
                sb.append(" ");
            }
            else if (bb.compareToIgnoreCase("v") == 0)
            {
                sb.append(context.getString(R.string.languageEsShort));
                sb.append(" ");
            }
            else if (bb.compareToIgnoreCase("l") == 0)
            {
                sb.append(context.getString(R.string.languageFrShort));
                sb.append(" ");
            }
            else if (bb.compareToIgnoreCase("d") == 0)
            {
                sb.append(context.getString(R.string.languageItShort));
                sb.append(" ");
            }
        }

        final String langStack = sb.toString().trim().replaceAll(" ", ", ");

        return langStack;
    }

    /**
     * Get THEME_NAME
     * @param context
     * @return LIGHT as default
     */
    protected static String GetPrefThemeName(final Context context)
    {
        String themeName = "DARK";

        try
        {
            themeName = PCommon.GetPref(context, APP_PREF_KEY.THEME_NAME, "LIGHT");

            switch (themeName)
            {
                case "LIGHT":
                    break;

                case "LIGHT_AND_BLUE":
                    break;

                case "DARK":
                    break;

                case "KAKI":
                    themeName = "LIGHT";
                    break;

                default:
                    themeName = "LIGHT";
                    break;
            }
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }

        return themeName;
    }

    /**
     * Set THEME_NAME
     * @param context   Context
     * @param themeName Theme
     */
    protected static void SetThemeName(final Context context, String themeName)
    {
        try
        {
            switch (themeName)
            {
                case "LIGHT":
                    break;

                case "LIGHT_AND_BLUE":
                    break;

                case "DARK":
                    break;

                case "KAKI":
                    themeName = "LIGHT";
                    break;

                default:
                    themeName = "LIGHT";
                    break;
            }

            PCommon.SavePref(context, APP_PREF_KEY.THEME_NAME, themeName);
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }
    }

    /**
     * Log (Release mode)
     * @param msg   Message
     */
    protected static void LogR(final Context context, final String msg)
    {
        final String newMsg = ConcaT(TimeFunc(), "  ", msg);

        //AddLog implementation
        CheckLocalInstance(context);
        _s.AddLog(newMsg);
    }

    /**
     * Log (Release mode)
     * @param resId RessourceId (without params)
     *
    protected static void LogR(final Context context, final int resId) {
        LogR(context, context.getResources().getText(resId).toString());
    }
    */

    /**
     * Log (Release mode)
     * @param resId RessourceId
     * @param args  Arguments (for String.format)
     */
    protected static void LogR(final Context context, final int resId, final Object... args) {
        LogR(context, String.format(context.getResources().getText(resId).toString(), args));
    }

    /**
     * Log (Release mode) stack trace
     * @param context   Context
     * @param ex    Exception
     */
    protected static void LogR(final Context context, final Exception ex)
    {
        LogR(context, R.string.logErr, StackTraceToString(ex.getStackTrace()));

        //LogR(context, ex, "");
    }

    /**
     * Log (Release mode) stack trace
     * @param context   Context
     * @param ex    Exception
     * @param addMsg    Additional message
     */
/*
    protected static void LogR(final Context context, final Exception ex, String addMsg)
    {
        String msg = null;
        String logs = null;
        String logsCut = null;

        try
        {
            //was LogR(context, R.string.logErr, StackTraceToString(ex.getStackTrace()));

            final String now = NowFunc();

            if (!_isDebugVersion)
            {
                //Release
                msg = ex.getMessage();
                logs = ConcaT(GetPref(context, APP_PREF_KEY.LOG_STATUS), "\n", now, "  ", msg);
            }
            else
            {
                //Debug
                if (addMsg == null || addMsg.length() == 0)
                {
                    addMsg = "?";
                }

                msg = StackTraceToString(ex.getStackTrace());
                logs = ConcaT(GetPref(context, APP_PREF_KEY.LOG_STATUS), "\n", now, "  ", addMsg, " <\n", msg);
            }

            logsCut = LogCut(logs);
            SavePref(context, APP_PREF_KEY.LOG_STATUS, logsCut);
        }
        catch(Exception ex2)
        {
            //Should do nothing
        }
        finally
        {
            msg = null;
            logs = null;
            logsCut = null;
        }
    }
*/

    /**
     * Log (Debug mode only)
     * @param msg   Message
     *
    protected static void LogD(final Context context, final String msg)
    {
        if (_isDebugVersion)
        {
            LogR(context, msg);
        }
    }
    */

    /**
     * Reduce logs
     * @param logs Logs
     * @return logs cut
     */
    /*
    protected static String LogCut(final String logs)
    {
        //TODO: external maxSize and put above
        final int size = logs.length();
        final int maxSize = 10000;

        if (size >= maxSize) {
            //Cut
            //TODO: begin after first /n, #### seems ok for end of string but check it.
            return logs.substring(size - maxSize);
        }

        return logs;
    }
    */

    /**
     * Clear logs (in variable)
     */
    protected static void ClearErrorLogs(final Context context)
    {
        try
        {
            CheckLocalInstance(context);    //20160728 added but bug sometimes!!!

            SavePref(context, APP_PREF_KEY.LOG_STATUS, "");

            _s.DeleteAllLogs();
        }
        catch(Exception ex)
        {
            if (_isDebugVersion) PCommon.LogR(context, ex);
        }
    }

    /***
     * Get resource threadId
     * @param context   Context
     * @param resName   Resource name
     * @return Resource Id (-1 by default)
     */
    protected static int GetResId(final Context context, final String resName)
    {
        int id = -1;

        try
        {
            //TODO: what to do if not found?
            if (resName == null || resName.length() == 0)
            {
                return id;
            }

            id = org.hlwd.bible.R.string.class.getField( resName ).getInt(null);
        }
        catch(Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }

        return id;
    }

    /***
     * Get drawable ciId
     * @param context   Context
     * @param resName   Resource name
     * @return Resource Id (-1 by default)
     */
    protected static int GetDrawableId(final Context context, final String resName)
    {
        int id = -1;

        try
        {
            //TODO: what to do if not found?
            if (resName == null || resName.length() == 0)
            {
                return id;
            }

            id = org.hlwd.bible.R.drawable.class.getField( resName ).getInt(null);
        }
        catch(Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }

        return id;
    }

    /***
     * Get resource (text)
     * @param resNm         Resource name
     * @param formatType    Format type: 0 = space (wide), 1 = newline (high)
     * @return resource as string (null if not found)
     *
    protected static String GetResText(final Context context, final String resNm, final int formatType) throws Exception
    {
        //Rem: different context; from Activity to Application, resource not available => give full path
        //Works with: org.me.vibrato.R.string.class.getField( resNm ).getInt(null);
        final int resId = (resNm == null) ? -1 : org.hlwd.bible.R.string.class.getField( resNm ).getInt(null);

        String resText = null;

        if (resNm == null)
        {
            resText = null;
        }
        else
        {
            resText = context.getResources().getText(resId).toString();

            //Replace | by new line.
            if (resText.length() > 0)
            {
                if (formatType == 1)
                    resText = resText.replaceAll("\\|", "\n");
                else
                    resText = resText.replaceAll("\\|", " ");
            }
        }

        return resText;
    }
    */

    /***
     * Modulo (works for negative value)
     * @param x     Value
     * @param mod   Modulo
     * @return -2 mod 6 => 4
     *
    protected static int Modulo(final int x, final int mod)
    {
        final int result = x % mod;

        return (result < 0) ? result + mod : result;
    }
    */

    /***
     * Get count of threads running
     * @param context   Context
     * @return Count of threads running
     */
    protected static int GetCountThreadRunning(final Context context)
    {
        int count = 0;

        try
        {
            final String threadName = context.getString(R.string.threadNfoPrefix);

            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            final Thread[] threadArr = threadSet.toArray(new Thread[threadSet.size()]);

            for (Thread thread : threadArr)
            {
                //TODO: ThreadGroup! => list group to find it?
                if (thread.getName().startsWith(threadName)) count++;
            }

            threadSet.clear();
            threadSet = null;
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }

        return count;
    }

    /***
     * Try to quit application
     * @param context   Context
     */
    protected static void TryQuitApplication(final Context context)
    {
        try
        {
            final int count = GetCountThreadRunning(context);
            if (count > 0)
            {
                ShowToast(context, R.string.installQuit, Toast.LENGTH_SHORT);
                return;
            }
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }
        finally
        { }

        QuitApplication(context);
    }

    /***
     * Quit application
     * @param context   Context
     */
    protected static void QuitApplication(final Context context)
    {
        try
        {
/*
            if (_s == null) CheckLocalInstance(context);

            _s.ShrinkDb(context);

            if (PCommon._isDebugVersion) System.out.print("Shrunk");
*/
            _s.CloseDb();
        }
        catch (Exception ex) { }
        finally
        {
            _s = null;
        }

        try
        {
            final int appId = android.os.Process.myPid();
            android.os.Process.killProcess(appId);
        }
        catch (Exception ex) { }
        finally
        { }
    }

    /**
     * Show notification
     * @param context       Context
     * @param title         Usually: appName
     * @param message       Message. Should be a (custom) message from resource file
     * @param drawable      Drawable Id
     */
    protected static void ShowNotification(final Context context, final String title, final String message, final int drawable)
    {
        NotificationManager nm = null;
        NotificationCompat.Builder notification = null;
        Intent intent = null;
        PendingIntent pIntent = null;

        try
        {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            intent = new Intent(context, MainActivity.class);
            pIntent = PendingIntent.getActivity(context, 0, intent, 0);

            notification = new NotificationCompat.Builder(context)
                    .setWhen(System.currentTimeMillis())
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(drawable);

            nm.notify(0, notification.build());
        }
        catch(Exception ex)
        {
            if (PCommon._isDebugVersion) LogR(context, ex);
        }
        finally
        {
            //Cleaning
            notification = null;
            intent = null;
            pIntent = null;
            nm = null;
        }
    }

    /***
     * Show Toast
     * @param context   Context
     * @param message   Message
     * @param duration  Duration (ex: Toast.LENGTH_SHORT...)
     */
    protected static void ShowToast(final Context context, final int message, final int duration)
    {
        final Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    /***
     * Copy text to clipboard
     * @param context   Context
     * @param label     Label
     * @param text      Text to copy
     */
    protected static void CopyTextToClipboard(final Context context, final String label, final String text)
    {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    /***
     * Send email
     * @param context   Context
     * @param toList    Array of email address
     * @param subject   Email subject
     * @param body      Email body
     */
    protected static void SendEmail(final Context context, final String[] toList, final String subject, final String body)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL  , toList);
            intent.putExtra(Intent.EXTRA_SUBJECT, (subject == null) ? "" : subject);
            intent.putExtra(Intent.EXTRA_TEXT   , (body == null) ? "" : body);
            intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);

            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.emailChooser)));
        }
        catch(Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }
    }

    /***
     * Share text
     * @param context   Context
     * @param text      Text to share
     */
    protected static void ShareText(final Context context, final String text)
    {
        try
        {
            final Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }
    }

    /***
     * Select bible language
     * Response in BIBLE_NAME_DIALOG
     * @param context
     * @param msg
     * @param desc
     * @param isCancelable
     * @param forceShowAllButtons  Force to show all buttons
     * @return builder
     */
    protected static void SelectBibleLanguage(final AlertDialog builder, final Context context, final View view, final String msg, final String desc, final boolean isCancelable, final boolean forceShowAllButtons)
    {
        try
        {
            builder.setCancelable(isCancelable);
            if (isCancelable) {
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "");
                    }
                });
            }
            builder.setTitle(msg);
            builder.setView(view);

            final int colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
            final String bbName = PCommon.GetPref(context, APP_PREF_KEY.BIBLE_NAME, "");
            final int installStatus = (forceShowAllButtons) ? 4 : Integer.parseInt(PCommon.GetPref(context, APP_PREF_KEY.INSTALL_STATUS, "1"));

            final Button btnLanguageEN = (Button) view.findViewById(R.id.btnLanguageEN);
            if (installStatus < 1) btnLanguageEN.setVisibility(View.INVISIBLE);
            if (bbName.compareToIgnoreCase("k") == 0) btnLanguageEN.setTextColor(colorAccent);
            btnLanguageEN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "k");
                    builder.dismiss();
                }
            });
            final Button btnLanguageES = (Button) view.findViewById(R.id.btnLanguageES);
            if (installStatus < 2) btnLanguageES.setVisibility(View.INVISIBLE);
            if (bbName.compareToIgnoreCase("v") == 0) btnLanguageES.setTextColor(colorAccent);
            btnLanguageES.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "v");
                    builder.dismiss();
                }
            });
            final Button btnLanguageFR = (Button) view.findViewById(R.id.btnLanguageFR);
            if (installStatus < 3) btnLanguageFR.setVisibility(View.INVISIBLE);
            if (bbName.compareToIgnoreCase("l") == 0) btnLanguageFR.setTextColor(colorAccent);
            btnLanguageFR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "l");
                    builder.dismiss();
                }
            });
            final Button btnLanguageIT = (Button) view.findViewById(R.id.btnLanguageIT);
            if (installStatus < 4) btnLanguageIT.setVisibility(View.INVISIBLE);
            if (bbName.compareToIgnoreCase("d") == 0) btnLanguageIT.setTextColor(colorAccent);
            btnLanguageIT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "d");
                    builder.dismiss();
                }
            });
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }
    }

    /***
     * Select multiple bible language
     * Response in TRAD_BIBLE_NAME
     * @param context
     * @param msg
     * @param desc
     * @param isCancelable
     * @param forceShowAllButtons  Force to show all buttons
     * @return builder
     */
    protected static void SelectBibleLanguageMulti(final AlertDialog builder, final Context context, final View view, final String msg, final String desc, final boolean isCancelable, final boolean forceShowAllButtons)
    {
        try
        {
            builder.setCancelable(isCancelable);
            if (isCancelable) {
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "");
                    }
                });
            }
            builder.setTitle(msg);
            builder.setView(view);

            final int colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
            final String bbName = PCommon.GetPrefBibleName(context);
            final int installStatus = (forceShowAllButtons) ? 4 : Integer.parseInt(PCommon.GetPref(context, APP_PREF_KEY.INSTALL_STATUS, "1"));

            final TextView tvTrad = (TextView) view.findViewById(R.id.tvTrad);
            final ToggleButton btnLanguageEN = (ToggleButton) view.findViewById(R.id.btnLanguageEN);
            if (installStatus < 1) btnLanguageEN.setEnabled(false);
            if (bbName.compareToIgnoreCase("k") == 0) btnLanguageEN.setTextColor(colorAccent);
            btnLanguageEN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "k"));
                    tvTrad.setText(languageStack);
                }
            });
            final ToggleButton btnLanguageES = (ToggleButton) view.findViewById(R.id.btnLanguageES);
            if (installStatus < 2) btnLanguageES.setEnabled(false);
            if (bbName.compareToIgnoreCase("v") == 0) btnLanguageES.setTextColor(colorAccent);
            btnLanguageES.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "v"));
                    tvTrad.setText(languageStack);
                }
            });
            final ToggleButton btnLanguageFR = (ToggleButton) view.findViewById(R.id.btnLanguageFR);
            if (installStatus < 3) btnLanguageFR.setEnabled(false);
            if (bbName.compareToIgnoreCase("l") == 0) btnLanguageFR.setTextColor(colorAccent);
            btnLanguageFR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "l"));
                    tvTrad.setText(languageStack);
                }
            });
            final ToggleButton btnLanguageIT = (ToggleButton) view.findViewById(R.id.btnLanguageIT);
            if (installStatus < 4) btnLanguageIT.setEnabled(false);
            if (bbName.compareToIgnoreCase("d") == 0) btnLanguageIT.setTextColor(colorAccent);
            btnLanguageIT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "d"));
                    tvTrad.setText(languageStack);
                }
            });
            final Button btnLanguageClear = (Button) view.findViewById(R.id.btnLanguageClear);
            if (installStatus <= 0) btnLanguageClear.setEnabled(false);
            btnLanguageClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //clear all & select default
                    final String currentTrad = "";
                    PCommon.SavePref(context, APP_PREF_KEY.TRAD_BIBLE_NAME, currentTrad);
                    btnLanguageEN.setChecked(false);
                    btnLanguageES.setChecked(false);
                    btnLanguageFR.setChecked(false);
                    btnLanguageIT.setChecked(false);
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, 0, ""));
                    tvTrad.setText(languageStack);
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "");
                }
            });
            final Button btnLanguageContinue = (Button) view.findViewById(R.id.btnLanguageContinue);
            if (installStatus <= 0) btnLanguageContinue.setEnabled(false);
            btnLanguageContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //all selected toggle
                    final String currentTrad = PCommon.GetPrefTradBibleName(context, false);
                    if (currentTrad == "") return;
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, currentTrad.substring(0, 1));
                    builder.dismiss();
                }
            });

            final String tradInit = PCommon.GetPrefTradBibleName(context, false);
            if (tradInit.indexOf("k") >= 0) btnLanguageEN.setChecked(true);
            if (tradInit.indexOf("v") >= 0) btnLanguageES.setChecked(true);
            if (tradInit.indexOf("l") >= 0) btnLanguageFR.setChecked(true);
            if (tradInit.indexOf("d") >= 0) btnLanguageIT.setChecked(true);
            final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, 0, ""));
            tvTrad.setText(languageStack);
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }
    }

    /***
     * Select multiple bible language and chapter
     * Response in TRAD_BIBLE_NAME
     * @param context
     * @param msg
     * @param desc
     * @param isCancelable
     * @param forceShowAllButtons  Force to show all buttons
     * @param chapterMax
     * @return builder
     */
    protected static void SelectBibleLanguageMultiChapter(final AlertDialog builder, final Context context, final View view, final String msg, final String desc, final boolean isCancelable, final boolean forceShowAllButtons, final int chapterMax)
    {
        try
        {
            builder.setCancelable(isCancelable);
            if (isCancelable) {
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "");
                    }
                });
            }
            builder.setTitle(msg);
            builder.setView(view);

            final int colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
            final String bbName = PCommon.GetPrefBibleName(context);
            final int installStatus = (forceShowAllButtons) ? 4 : Integer.parseInt(PCommon.GetPref(context, APP_PREF_KEY.INSTALL_STATUS, "1"));
            final TextView tvTrad = (TextView) view.findViewById(R.id.tvTrad);
            final NumberPicker npChapter = (NumberPicker) view.findViewById(R.id.npChapter);
            npChapter.setMinValue(1);
            npChapter.setMaxValue(chapterMax);

            final ToggleButton btnLanguageEN = (ToggleButton) view.findViewById(R.id.btnLanguageEN);
            if (installStatus < 1) btnLanguageEN.setEnabled(false);
            if (bbName.compareToIgnoreCase("k") == 0) btnLanguageEN.setTextColor(colorAccent);
            btnLanguageEN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "k"));
                    tvTrad.setText(languageStack);
                }
            });
            final ToggleButton btnLanguageES = (ToggleButton) view.findViewById(R.id.btnLanguageES);
            if (installStatus < 2) btnLanguageES.setEnabled(false);
            if (bbName.compareToIgnoreCase("v") == 0) btnLanguageES.setTextColor(colorAccent);
            btnLanguageES.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "v"));
                    tvTrad.setText(languageStack);
                }
            });
            final ToggleButton btnLanguageFR = (ToggleButton) view.findViewById(R.id.btnLanguageFR);
            if (installStatus < 3) btnLanguageFR.setEnabled(false);
            if (bbName.compareToIgnoreCase("l") == 0) btnLanguageFR.setTextColor(colorAccent);
            btnLanguageFR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "l"));
                    tvTrad.setText(languageStack);
                }
            });
            final ToggleButton btnLanguageIT = (ToggleButton) view.findViewById(R.id.btnLanguageIT);
            if (installStatus < 4) btnLanguageIT.setEnabled(false);
            if (bbName.compareToIgnoreCase("d") == 0) btnLanguageIT.setTextColor(colorAccent);
            btnLanguageIT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    final int op = compoundButton.isChecked() ? 1 : -1;
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, op, "d"));
                    tvTrad.setText(languageStack);
                }
            });
            final Button btnLanguageClear = (Button) view.findViewById(R.id.btnLanguageClear);
            if (installStatus <= 0) btnLanguageClear.setEnabled(false);
            btnLanguageClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //clear all & select default
                    final String currentTrad = "";
                    PCommon.SavePref(context, APP_PREF_KEY.TRAD_BIBLE_NAME, currentTrad);
                    btnLanguageEN.setChecked(false);
                    btnLanguageES.setChecked(false);
                    btnLanguageFR.setChecked(false);
                    btnLanguageIT.setChecked(false);
                    final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, 0, ""));
                    tvTrad.setText(languageStack);
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, "");
                }
            });
            final Button btnLanguageContinue = (Button) view.findViewById(R.id.btnLanguageContinue);
            if (installStatus <= 0) btnLanguageContinue.setEnabled(false);
            btnLanguageContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //all selected toggle
                    final String currentTrad = PCommon.GetPrefTradBibleName(context, false);
                    if (currentTrad == "") return;
                    PCommon.SavePref(context, APP_PREF_KEY.BIBLE_NAME_DIALOG, currentTrad.substring(0, 1));
                    PCommon.SavePref(context, APP_PREF_KEY.BOOK_CHAPTER_DIALOG, String.valueOf(npChapter.getValue()));
                    builder.dismiss();
                }
            });

            final String tradInit = PCommon.GetPrefTradBibleName(context, false);
            if (tradInit.indexOf("k") >= 0) btnLanguageEN.setChecked(true);
            if (tradInit.indexOf("v") >= 0) btnLanguageES.setChecked(true);
            if (tradInit.indexOf("l") >= 0) btnLanguageFR.setChecked(true);
            if (tradInit.indexOf("d") >= 0) btnLanguageIT.setChecked(true);
            final String languageStack = PCommon.ConcaT(context.getString(R.string.tvTrad), " ", PCommon.ManageTradBibleName(context, 0, ""));
            tvTrad.setText(languageStack);
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }
    }

    /***
     * Show simple dialog
     * @param activity
     * @param titleId
     * @param msgId
     */
    protected static void ShowDialog(final Activity activity, final int titleId, final int msgId)
    {
        try
        {
            final LayoutInflater inflater = activity.getLayoutInflater();
            final View view = inflater.inflate(R.layout.fragment_dialog, (ViewGroup) activity.findViewById(R.id.llDialog));

            final AlertDialog builder = new AlertDialog.Builder(activity).create();
            builder.setCancelable(false);
            builder.setTitle(titleId);
            builder.setView(view);

            final TextView tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            tvMsg.setText(msgId);

            final Button btnClose = (Button) view.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    builder.dismiss();
                }
            });

            builder.show();
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(activity.getApplicationContext(), ex);
        }
    }

    /***
     * Get random int value in ranges [min, max]
     * @param context
     * @param minRange  Minimum
     * @param maxRange  Maximum
     * @return Random int
     */
    protected static int GetRandomInt(final Context context, final int minRange, final int maxRange)
    {
        int rndValue = minRange;

        try
        {
            if (maxRange <= 0) throw new Exception("Range invalid!");

            final int range = maxRange - minRange + 1;
            final Random randomGenerator = new Random(System.currentTimeMillis());
            rndValue = randomGenerator.nextInt(range);  //from 0..n-1
            rndValue += minRange;
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }

        return rndValue;
    }

    /***
     * Get typeface
     * @param context
     * @return null has default typeface, so don't set it. Roboto?
     */
    protected static Typeface GetTypeface(final Context context)
    {
        try
        {
            final String tfName = PCommon.GetPref(context, APP_PREF_KEY.FONT_NAME, "");
            final Typeface tf = (tfName == null || tfName.length() == 0)
                    ? Typeface.defaultFromStyle(Typeface.NORMAL)
                    : Typeface.createFromAsset(context.getAssets(), PCommon.ConcaT("fonts/", tfName, ".ttf"));

            return tf;
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }

        return null;
    }

    /***
     * Get font size (verse)
     * @param context
     */
    protected static int GetFontSize(final Context context)
    {
        try
        {
            final int fontSize = Integer.parseInt(PCommon.GetPref(context, APP_PREF_KEY.FONT_SIZE, "14"));
            return fontSize;
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(context, ex);
        }

        return 14;
    }
}