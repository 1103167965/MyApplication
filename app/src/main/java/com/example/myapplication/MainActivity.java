package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static char va[] ={'0','1','2','3','4','5','6','7','8','9','+','-','*','/','.'};
    static int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt[] = new Button[16];
        bt[0] = findViewById(R.id.button0);
        bt[1] = findViewById(R.id.button1);
        bt[2] = findViewById(R.id.button2);
        bt[3] = findViewById(R.id.button3);
        bt[4] = findViewById(R.id.button4);
        bt[5] = findViewById(R.id.button5);
        bt[6] = findViewById(R.id.button6);
        bt[7] = findViewById(R.id.button7);
        bt[8] = findViewById(R.id.button8);
        bt[9] = findViewById(R.id.button9);
        bt[10] = findViewById(R.id.buttonjia);
        bt[11] = findViewById(R.id.buttonjian);
        bt[12] = findViewById(R.id.buttoncheng);
        bt[13] = findViewById(R.id.buttonchu);
        bt[14] = findViewById(R.id.buttondian);
        bt[15] = findViewById(R.id.buttondeng);
        for (i = 0; i < 15; i++)
            bt[i].setOnClickListener(new View.OnClickListener() {
                final char id=MainActivity.va[MainActivity.i];
                @Override
                public void onClick(View v) {
                    // Perform action on click
                    //Toast.makeText(MainActivity.this, "btn is clicked!", Toast.LENGTH_SHORT).show();
                    TextView tv = findViewById(R.id.textView);
                    tv.setText(tv.getText().toString() + id);
                }
            });
        bt[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action on click
                //Toast.makeText(MainActivity.this, "btn is clicked!", Toast.LENGTH_SHORT).show();
                TextView tv = findViewById(R.id.textView);
                String val=tv.getText().toString();
                System.out.println(tv.getText().toString());
                System.out.println(calculate(tv.getText().toString()));
                tv.setText(calculate(tv.getText().toString()));
            }
        });
    }

    public char add = '+';

    public char del = '-';

    public char mul = '*';

    public char div = '/';

    public String calculate(String s){
        StringBuffer sbMath = new StringBuffer();
        List<String> math = new ArrayList<String>();
        List<String> flag = new ArrayList<String>();
        List<Integer> mulDiv = new ArrayList<Integer>();
        for (int i = 0; i < s.length(); i++) {
            char temp = s.charAt(i);
            if(temp!= add && temp!= del && temp!=mul && temp!=div){
                sbMath.append(String.valueOf(temp));
            }else{
                if(sbMath.length()==0 && temp==del){
                    sbMath.append("0");
                }
                math.add(sbMath.toString());
                sbMath.delete(0, sbMath.length());
                flag.add(String.valueOf(temp));
                if(temp == mul || temp == div){
                    mulDiv.add(flag.size()-1);
                }
            }
        }
        math.add(sbMath.toString());
        while(math.size() != 1){
            boolean needReIndex = false;
            while(mulDiv.size() != 0){
                int index = mulDiv.get(0);
                if(needReIndex){
                    index = index -1;
                }
                Map<String, List<String>> map = this.loopProcess(index, math, flag);
                math = map.get("math");
                flag = map.get("flag");
                mulDiv = this.removeList(Integer.class, mulDiv, 0);
                needReIndex = true;
            }
            while(flag.size() != 0){
                Map<String, List<String>> map = this.loopProcess(0, math, flag);
                math = map.get("math");
                flag = map.get("flag");
            }
        }
        return math.get(0);
    }

    private Map<String, List<String>> loopProcess(int index, List<String> math, List<String> flag){
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        char ch = flag.get(index).charAt(0);
        String result = this.getResult(math.get(index).trim(), math.get(index+1).trim(), ch);
        math = this.removeList(String.class, math, index);
        math = this.removeList(String.class, math, index);
        math.add(index, result);
        flag = this.removeList(String.class, flag, index);
        map.put("math", math);
        map.put("flag", flag);
        return map;
    }

    private <T> List<T> removeList(Class<T> clazz, List<T> list, int index){
        List<T> listTemp = new ArrayList<T>();
        for (int i = 0; i < list.size(); i++) {
            if(i != index){
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }

    private String getResult(String b, String e, char flag){
        boolean isLong = false;
        if(!b.contains(".") && !e.contains(".")){
            isLong = true;
        }
        if(isLong){
            if(flag == add){
                return String.valueOf(Long.valueOf(b)+Long.valueOf(e));
            }else if(flag == del){
                return String.valueOf(Long.valueOf(b)-Long.valueOf(e));
            }else if(flag == mul){
                return String.valueOf(Long.valueOf(b)*Long.valueOf(e));
            }else if(flag == div){
                return String.valueOf((double)Long.valueOf(b)/Long.valueOf(e));
            }else{
                throw new RuntimeException("error: "+ b + flag + e);
            }
        }else{
            if(flag == add){
                return String.valueOf(Double.valueOf(b)+Double.valueOf(e));
            }else if(flag == del){
                return String.valueOf(Double.valueOf(b)-Double.valueOf(e));
            }else if(flag == mul){
                return String.valueOf(Double.valueOf(b)*Double.valueOf(e));
            }else if(flag == div){
                return String.valueOf((double)Double.valueOf(b)/Double.valueOf(e));
            }else{
                throw new RuntimeException("error: "+ b + flag + e);
            }
        }

    }

}