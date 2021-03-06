package com.example.student.dadajo;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.bumptech.glide.Glide;

import java.io.IOException;

import retrofit2.Response;

public class SettingActivity extends PreferenceActivity {

     SwitchPreference rainSetting;
     SwitchPreference dustSetting;
    static int rainSettingState=0;
    static int dustSettingState=0;

    SwitchPreference windowAlarm;
    SwitchPreference dustAlarm;
    SwitchPreference rainAlarm;
    static int windowAlarmState=0;
    static int dustAlarmState=0;
    static int rainAlarmState=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top

        rainSetting = (SwitchPreference)findPreference("switch_preference_3");
        dustSetting = (SwitchPreference)findPreference("switch_preference_2");
        windowAlarm = (SwitchPreference)findPreference("switch_preference_4");
        dustAlarm = (SwitchPreference)findPreference("switch_preference_5");
        rainAlarm = (SwitchPreference)findPreference("switch_preference_6");

        dustSetting.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean switched = ((SwitchPreference) preference).isChecked();

                if(switched){
                    dustSettingState=0;


                }else{
                    dustSettingState=1;

                }

                Log.d("dustSettingState","dustSettingState: "+dustSettingState);


                updateSetting();
                return true;
            }
        });



        rainSetting.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

              @Override
              public boolean onPreferenceChange(Preference preference, Object newValue) {
                  boolean switched = ((SwitchPreference) preference).isChecked();

                  if(switched){
                      rainSettingState=0;
                  }else{
                      rainSettingState=1;
                  }

                  Log.d("rainSettingState","rainSettingState: "+rainSettingState);

                  updateSetting();


                  return true;
              }

          });

        windowAlarm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean switched = ((SwitchPreference) preference).isChecked();

                if(switched){
                    windowAlarmState=0;
                }else{
                    windowAlarmState=1;
                }

                return true;
            }
        });

        dustAlarm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean switched = ((SwitchPreference) preference).isChecked();

                if(switched){
                    dustAlarmState=0;
                }else{
                    dustAlarmState=1;
                }

                return true;
            }
        });

        rainAlarm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean switched = ((SwitchPreference) preference).isChecked();

                if(switched){
                    rainAlarmState=0;
                }else{
                    rainAlarmState=1;
                }

                return true;
            }
        });



        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void updateSetting(){
        new Thread() {
            public void run() {
                try {
                    Response<Boolean> res = SensorApi.service.putDust(dustSettingState).execute(); // 현재 스레드에서 네트워크 작업 요청.
                    if(res.code()==200) {
                        Boolean result = res.body();
                        if(result) {
                            //System.out.println("window 가져오기 실패");
                            Log.d("결과","dustSetting 보내기 성공");
                        }else {
                            // System.out.println("window 가져오기 성공");
                            Log.d("결과","dustSetting 보내기 실패 " + result);
                        }
                    }else {
                        // System.out.println("에러 코드: "+res.code());
                        Log.d("결과","에러 코드: "+res.code());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("결과","예외 발생: "+e.getMessage());
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    Response<Boolean> res = SensorApi.service.putRain(rainSettingState).execute(); // 현재 스레드에서 네트워크 작업 요청.
                    if(res.code()==200) {
                        Boolean result = res.body();
                        if(result) {
                            //System.out.println("window 가져오기 실패");
                            Log.d("결과","rainSetting 보내기 성공");
                        }else {
                            // System.out.println("window 가져오기 성공");
                            Log.d("결과","rainSetting 보내기 실패 " + result);
                        }
                    }else {
                        // System.out.println("에러 코드: "+res.code());
                        Log.d("결과","에러 코드: "+res.code());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        if(dustSettingState==1&&rainSettingState==1){//둘 다 켜졌을 때
            FirstFragment.switchWindow.setEnabled(false);
        }else if(dustSettingState==1&&rainSettingState==0){
            FirstFragment.switchWindow.setEnabled(false);
        }else if(dustSettingState==0&&rainSettingState==1){
            FirstFragment.switchWindow.setEnabled(false);
        }else{
            FirstFragment.switchWindow.setEnabled(true);
        }
    };



}
