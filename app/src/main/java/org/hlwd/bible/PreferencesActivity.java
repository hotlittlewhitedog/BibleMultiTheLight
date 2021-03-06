
package org.hlwd.bible;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PreferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);

            final int themeId = PCommon.GetPrefThemeId(getApplicationContext());
            setTheme(themeId);

            getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment(), "PA").commit();
        }
        catch (Exception ex)
        {
            if (PCommon._isDebugVersion) PCommon.LogR(getApplicationContext(), ex);
        }
    }
}
