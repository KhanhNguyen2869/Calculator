/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;
//22
import gui.GUICaculator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author Kero
 */
public class Control implements ActionListener {

    private GUICaculator gc;
    private boolean process = false;//check khi nào người dùng nhập số mới
    private int operate = -1; //đại diện + - * /
    private BigDecimal firstNum;
    private BigDecimal secondNum;
    private boolean reset = false;
    private boolean isMR = false;
    private BigDecimal memory = new BigDecimal("0");

    public Control(GUICaculator gc) {
        this.gc = gc;
        PressNumber();
    }

    public void PressNumber() {
        gc.getBtn0().addActionListener(this);
        gc.getBtn1().addActionListener(this);
        gc.getBtn2().addActionListener(this);
        gc.getBtn3().addActionListener(this);
        gc.getBtn4().addActionListener(this);
        gc.getBtn5().addActionListener(this);
        gc.getBtn6().addActionListener(this);
        gc.getBtn7().addActionListener(this);
        gc.getBtn8().addActionListener(this);
        gc.getBtn9().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String txt = e.getActionCommand();
        if (process || reset) {
            gc.getTxtScreen().setText("0");
            process = false;
            reset = false;
        }
        isMR = false;
        String temp = gc.getTxtScreen().getText() + txt;
        BigDecimal number = new BigDecimal(temp);
        gc.getTxtScreen().setText(number + "");
    }

    public void Operator(int operate) {
        Calculator();
        this.operate = operate;
        process = true;

    }

    public BigDecimal getNumber() {
        if (isMR) {
            return memory;
        }
        String value = gc.getTxtScreen().getText();

        BigDecimal temp;
        try {
            temp = new BigDecimal(value);
        } catch (Exception ex) {
            return firstNum;
        }
        return temp;
    }

    public void pressResult() {
        Calculator();
        operate = -1;
    }

    public void pressClear() {
        operate = -1;
        firstNum = new BigDecimal("0");
        process = false;
        gc.getTxtScreen().setText("0");
    }

    public void Calculator() {

        if (!process) {
            if (operate == -1) {
                firstNum = getNumber();
            } else {
                secondNum = getNumber();
                switch (operate) {
                    case 1:
                        firstNum = firstNum.add(secondNum);
                        break;
                    case 2:
                        firstNum = firstNum.subtract(secondNum);
                        break;
                    case 3:
                        firstNum = firstNum.multiply(secondNum);
                        break;
                    case 4:

                        if (secondNum.doubleValue() == 0) {
                            gc.getTxtScreen().setText("ERROR");
                            process = true;
                            return;

                        }
                        firstNum = firstNum.divide(secondNum, 15, RoundingMode.HALF_UP).stripTrailingZeros();
                        break;

                }
            }
            gc.getTxtScreen().setText(firstNum + "");
            process = true;
        }

    }

    public String getCurrentText() {
        return gc.getTxtScreen().getText();
    }

    public void delete() {
        if (!process) {
            String str = getCurrentText();
            String result = "0";
            if (str.length() > 1) {
                result = str.substring(0, str.length() - 1);
            }
            if (str.length() == 1) {
                result = "0";
            } else if (str.startsWith("-") && str.length() == 2) {
                result = "0";
            }
            gc.getTxtScreen().setText(result);
        }
    }

    public void Percent() {
        double result = getNumber().doubleValue() / 100;
        gc.getTxtScreen().setText(result + "");
        process = false;
        reset = true;

    }

    public void Invert() {
        if (getNumber().doubleValue() == 0) {
            gc.getTxtScreen().setText("ERROR");
        } else {
            gc.getTxtScreen().setText((1 / getNumber().doubleValue()) + "");
        }
        process = true;
    }

    public void pressDot() {
        if (!gc.getTxtScreen().getText().contains(".") && process == false) {
            gc.getTxtScreen().setText(getCurrentText() + ".");
            process = false;
        }
        if (process == true) {
            gc.getTxtScreen().setText("0.");
            process = false;
        }
    }

    public void pressNegate() {
        pressResult();
        gc.getTxtScreen().setText(getNumber().negate() + "");
        process = false;
        reset = true;
    }

    public void pressSqrt() {
        pressResult();
        BigDecimal result = getNumber();
        if (result.doubleValue() >= 0) {
            String display = Math.sqrt(result.doubleValue()) + "";
            if (display.endsWith("")) {
                display = display.replace(".0", "");
            }
            gc.getTxtScreen().setText(display);
            process = false;
        } else {
            gc.getTxtScreen().setText("ERROR");
        }
        reset = true;
    }

    public void pressMR() {
        gc.getTxtScreen().setText(memory + "");
        isMR = true;
        reset = true;

    }
    
    public void pressMS(){
        reset = true;
        memory = new BigDecimal(getCurrentText());
    }

    //MC : xóa bỏ trong bộ nhớ xét memory =0
    public void pressMC() {
        memory = new BigDecimal("0");
    }

    //ấn số: => M+ : lưu giá trị đó vào memory
    public void pressMAdd() {
        memory = memory.add(getNumber());
        process = false;

    }

    //ấn số: => M- : lưu giá trị đối của nó vào memory
    public void pressMSub() {
        memory = memory.add(getNumber().negate());
        process = false;
        reset = true;
    }
}
