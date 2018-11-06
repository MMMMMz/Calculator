package com.experiment;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class calculators extends JFrame implements ActionListener{
	
	public final String[] KEYS = {
			"7", "8", "9", "/", "sqrt", 
			"4", "5", "6","*", "%", 
			"1", "2", "3", "-", "1/x", 
			"0", "+/-", ".", "+", "="
			}; //计算器上的所有按键
	private final String[] COMMAND = {
			"Backspace", "C","换肤" ,"更换主题"
			};	//计算器上的功能键
	private JButton keys[] = new JButton[KEYS.length];	//计算器上的按钮
	private JButton commands[] = new JButton[COMMAND.length];	//计算器上的功能键
	private JTextField resultText = new JTextField("0",50);	//计算结果文本框
	
	// 标志用户按的是否是整个表达式的第一个数字,或者是运算符后的第一个数字
    private boolean firstDigit = true;
    // 计算的中间结果。
    private double resultNum = 0.0;
    // 当前运算的运算符
    private String operator = "=";
    // 操作是否合法
    private boolean operateValidFlag = true;
	
    private JPanel panel1 = new JPanel();	//最后的大的画板
    
    JPanel commandsPanel = new JPanel();	//初始化功能键
    
    JPanel calckeysPanel = new JPanel();	//初始化计算器上的按键,放在一个画板上
    
    Stack<Double> numStack = new Stack<Double>(); //数字栈
    
	Stack<String> operatorStack = new Stack<String>(); // 运算符栈		
	
	public calculators()	//构造函数
	{
		init();
		this.setTitle("MaZhe的计算器");	//设置计算器的标题
		this.setBackground(Color.BLACK);	//设置背景颜色
		this.setSize(500, 500);
		this.setLocation(500, 300);	//设置计算器在屏幕上的位置
		this.setResizable(false);	//设置计算器的大小不可修改
		setLocationByPlatform(true);	//主窗口出现根据平台习惯
		String className = "javax.swing.plaf.nimbus.NimbusLookAndFeel";	//更换观感
		try { 
			UIManager.setLookAndFeel(className); SwingUtilities.updateComponentTreeUI(this);
		} catch(Exception e){
			e.printStackTrace();
		}
//		this.pack();//自动调整组件大小
	}
	
	public void init()	//初始化函数
	{
		resultText.setHorizontalAlignment(JTextField.RIGHT);	//设置文本框内容为右对齐
		resultText.setEditable(false);
		resultText.setFont(new Font("Monospaced", Font.PLAIN, 35));
		resultText.setBackground(Color.WHITE);	//设置文本框的背景颜色为白色
		
		calckeysPanel.setLayout(new GridLayout(4, 5, 5, 5));	//设置按键布局为网格布局,为四行五列
		for(int i = 0 ; i < KEYS.length ; i++)
		{
			keys[i] = new JButton(KEYS[i]);	//循环添加按钮
			keys[i].setFont(new Font("Monospaced", Font.PLAIN, 20));	//设置每一个按钮的字体
			calckeysPanel.add(keys[i]);	//将按钮添加到容器中
			keys[i].setForeground(Color.WHITE);	//将计算器上的按键设置为蓝色
			keys[i].setBackground(Color.DARK_GRAY);	//将按键的背景颜色设置为深灰色
		}
		// 运算符键的背景颜色为橙色
        keys[3].setForeground(Color.ORANGE);
        keys[8].setForeground(Color.ORANGE);
        keys[13].setForeground(Color.ORANGE);
        keys[18].setForeground(Color.ORANGE);
        keys[19].setForeground(Color.ORANGE);
        
        
        commandsPanel.setLayout(new GridLayout(1, 3, 4, 4));	//设置功能键布局为网格式布局,一行三列
        for (int i = 0; i < COMMAND.length; i++) {
            commands[i] = new JButton(COMMAND[i]);
            commands[i].setFont(new Font("Monospaced", Font.PLAIN, 20));
            commandsPanel.add(commands[i]);
            commands[i].setForeground(Color.BLACK);
            commands[i].setBackground(Color.LIGHT_GRAY);
        }
        
        // 下面进行计算器的整体布局，将calckeys和command画板放在计算器的中部，

        // 新建一个大的画板，将上面建立的command和calckeys画板放在该画板内
 
        // 画板采用边界布局管理器，画板里组件之间的水平和垂直方向上间隔都为4象素
        panel1.setLayout(new BorderLayout(4, 4));
        panel1.add("North", commandsPanel);
        panel1.add("Center", calckeysPanel);
        
        //建立一个顶部的文本框
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add("Center", resultText);
        
        // 整体布局
        getContentPane().setLayout(new BorderLayout(3, 5));
        getContentPane().add("North", top);
        getContentPane().add("Center", panel1);
        
        // 为各按钮添加事件侦听器
        // 都使用同一个事件侦听器，即本对象
        for (int i = 0; i < KEYS.length; i++) {
            keys[i].addActionListener(this);
        }
        for (int i = 0; i < COMMAND.length; i++) {
            commands[i].addActionListener(this);
        }
	}
	
	
	 /**
     * 处理事件
     */
    public void actionPerformed(ActionEvent e) {
        // 获取事件源的标签
        String label = e.getActionCommand();
        if (label.equals(COMMAND[0])) {
            // 用户按了"Backspace"键
            handleBackspace();
        } else if (label.equals(COMMAND[1])) {
            // 用户按了"C"键
            resultText.setText("0");
        } else if(label.equals(COMMAND[2])){
        	//用户按了改变颜色键
        	handlechangecolor(e);
        }else if(label.equals(COMMAND[3])){
        	//用户按了改变颜色键
        	handlechangetheme(e);
        }else if ("0123456789.".indexOf(label) >= 0) {
            // 用户按了数字键或者小数点键
            handleNumber(label);
        } else {
            // 用户按了运算符键
            handleOperator(label);
        }
    }
 
    /**
     * 处理Backspace键被按下的事件
     */
    private void handleBackspace() {
        String text = resultText.getText();
        int i = text.length();
        if (i > 0) {
            // 退格，将文本最后一个字符去掉
            text = text.substring(0, i - 1);
            if (text.length() == 0) {
                // 如果文本没有了内容，则初始化计算器的各种值
                resultText.setText("0");
                firstDigit = true;
                operator = "=";
            } else {
                // 显示新的文本
                resultText.setText(text);
            }
        }
    }
    
    /**
     * 处理更换主题
     * @param e
     */
    private void handlechangetheme(ActionEvent e) {
    	// 列出安装的所有观感
    		LookAndFeelInfo[] looksinfo = UIManager.getInstalledLookAndFeels();
    	// 获取观感类名和名字
    		int num = new Random().nextInt(5);
    		String laf = looksinfo[num].getClassName();
		try {
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

    }
    
    /**
     * 	处理更换背景颜色
     */
    private void handlechangecolor(ActionEvent e) {
    	int num1 = new Random().nextInt(255);	//获取三个随机数来随机获取一种颜色
    	int num2 = new Random().nextInt(255);
    	int num3 = new Random().nextInt(255);
    	Color c = new Color(num1,num2,num3);
    	panel1.setBackground(c);
    	commandsPanel.setBackground(c);
    	calckeysPanel.setBackground(c);
    }
    
    /**
     * 处理数字键被按下的事件
     *
     * @param key
     */
    private void handleNumber(String key) {
        if (firstDigit) {
            // 输入的第一个数字
            resultText.setText(key);
            numStack.push(Double.valueOf(resultText.getText()).doubleValue());	//输入的操作数是整数就将其压入栈中
         
        } else if ((key.equals(".")) && (resultText.getText().indexOf(".") < 0)) {
            // 输入的是小数点，并且之前没有小数点，则将小数点附在结果文本框的后面
            resultText.setText(resultText.getText() + ".");		//如果是个小数,就将之前的整数出栈
            numStack.pop();
        } else if (!key.equals(".")) {
            // 如果输入的不是小数点，则将数字附在结果文本框的后面
            resultText.setText(resultText.getText() + key);
            numStack.push(Double.valueOf(resultText.getText()).doubleValue());	
        }
        // 以后输入的肯定不是第一个数字了
        firstDigit = false;
    }
 
    /**
     * 处理C键被按下的事件
     */
    private void handleC() {
        // 初始化计算器的各种值
        resultText.setText("0");
        firstDigit = true;
        operator = "=";
    }
 
    /**
     * 处理运算符键被按下的事件
     *
     * @param key
     */
    private void handleOperator(String key) {
        if (operator.equals("/")) {
            // 除法运算
            // 如果当前结果文本框中的值等于0
            if (getNumberFromText() == 0.0) {
                // 操作不合法
                operateValidFlag = false;
                resultText.setText("除数不能为零");
            } else {
                resultNum /= getNumberFromText();
            }
        } else if (operator.equals("1/x")) {
            // 倒数运算
            if (resultNum == 0.0) {
                // 操作不合法
                operateValidFlag = false;
                resultText.setText("零没有倒数");
            } else {
                resultNum = 1 / resultNum;
            }
        } else if (operator.equals("+")) {
            // 加法运算
            resultNum += getNumberFromText();
        } else if (operator.equals("-")) {
            // 减法运算
            resultNum -= getNumberFromText();
        } else if (operator.equals("*")) {
            // 乘法运算
            resultNum *= getNumberFromText();
        } else if (operator.equals("sqrt")) {
            // 平方根运算
            resultNum = Math.sqrt(resultNum);
        } else if (operator.equals("%")) {
            // 百分号运算，除以100
            resultNum = resultNum / 100;
        } else if (operator.equals("+/-")) {
            // 正数负数运算
            resultNum = -resultNum;
        } else if (operator.equals("=")) {
            // 赋值运算
            resultNum = getNumberFromText();
        }
        if (operateValidFlag) {	//如果是整数则显示整数,若不是则显示浮点数
            // 双精度浮点数的运算
            long t1;
            double t2;
            t1 = (long) resultNum;
            t2 = resultNum - t1;
            if (t2 == 0) {
                resultText.setText(String.valueOf(t1));
            } else {
                resultText.setText(String.valueOf(resultNum));
            }
        }
        // 运算符等于用户按的按钮
        operator = key;
        operatorStack.push(operator);
        firstDigit = true;
        operateValidFlag = true;
    }
	
    /**
     * 将从文本框中获取的字符串转化为浮点数
     * @return
     */
    private double getNumberFromText() {
        double result = 0;
        try {
            result = Double.valueOf(resultText.getText()).doubleValue();
        } catch (NumberFormatException e) {
        }
        return result;
    }
	
	public static void main(String[] args) {
		EventQueue.invokeLater(()->
		{
			calculators calculator1 = new calculators();
	        calculator1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        calculator1.setVisible(true);
		});
	}
}
