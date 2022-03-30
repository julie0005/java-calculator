package com.programmers.java.engine;

import com.programmers.java.engine.io.Input;
import com.programmers.java.engine.io.Output;
import com.programmers.java.engine.model.Calculator;
import com.programmers.java.engine.model.Expression;
import com.programmers.java.engine.model.History;
import lombok.AllArgsConstructor;

enum Menu{
    EXIT(0, "종료"), LOOKUP(1, "조회"), CALCULATE(2, "계산");

    private final int value;
    private final String prompt;

    Menu(int value, String prompt) {
        this.value=value;
        this.prompt=prompt;
    }

    @Override
    public String toString(){
        return prompt;
    }
}

//TODO : Exception과 에러 처리
//TODO : Parsing과 유효성 검사
//TODO : Parsing Test 코드 작성

@AllArgsConstructor
public class Lobby implements Runnable{

    private Input input;
    private Output output;
    private History history;
    private Calculator calculator;

    @Override
    public void run() {
        String menus="";
        //+= 사용은 메모리적인 측면에서 비효율적이지만 초기 부분에서만 일어나기 때문에 사용함.
        for(Menu m : Menu.values()){
            menus+=m.ordinal()+"."+m.toString()+"\n";
        }

        final int EXIT=Menu.EXIT.ordinal();
        final int LOOKUP=Menu.LOOKUP.ordinal();
        final int CALCULATE=Menu.CALCULATE.ordinal();

        while(true){
            System.out.println('\n'+menus);
            int userOption=input.optionInput("선택 : ");
            if(userOption==EXIT){
                output.exitMessage();
                break;
            }
            else if(userOption==LOOKUP){
                history.searchAll();
            }
            else if(userOption==CALCULATE){
                String userStr=input.strInput("계산식을 입력해주세요 : ");
                String[] parsedUserStr=parse(userStr);
                String ans=calculator.calculate(parsedUserStr);
                System.out.println(ans);
                history.save(new Expression(userStr,ans));
            }
            else{
                output.inputError();
                continue;
            }
        }
    }

    public String[] parse(String userStr){
        return userStr.split(" ");
    }
}
