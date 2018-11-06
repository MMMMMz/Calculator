package com.experiment;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class calculatorTemp extends JFrame implements ActionListener{
	
	public final String[] KEYS = {
			"7", "8", "9", "/", "sqrt", 
			"4", "5", "6","*", "%", 
			"1", "2", "3", "-", "1/x", 
			"0", "+/-", ".", "+", "="
			}; //计算器上的所有按键
	private final String[] COMMAND = {
			"Backspace", "C","换肤","更换主题"
			};	//计算器上的功能键
	private JButton keys[] = new JButton[KEYS.length];	//计算器上的按钮
	private JButton commands[] = new JButton[COMMAND.length];	//计算器上的功能键
	private static JTextField resultText = new JTextField("0",50);	//计算结果文本框
	
	// 标志用户按的是否是整个表达式的第一个数字,或者是运算符后的第一个数字
    private boolean firstDigit = true;
    // 计算的中间结果。
    private BigDecimal resultNum = new BigDecimal(0);
    // 当前运算的运算符
    private String operator = "=";
    // 操作是否合法
    private static boolean operateValidFlag = true;
	
    private JPanel panel1 = new JPanel();	//最后的大的画板
    
    JPanel commandsPanel = new JPanel();	//初始化功能键
    
    JPanel calckeysPanel = new JPanel();	//初始化计算器上的按键,放在一个画板上
	
	String exp = "";
	
	private static String ans = "0"; // 计算出的结果
	
	public calculatorTemp()	//构造函数
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
        	handleC();
        } else if(label.equals(COMMAND[2])){
        	//用户按了改变颜色键
        	handlechangecolor(e);
        }else if(label.equals(COMMAND[3])){
        	//用户按了改变颜色键
        	handlechangetheme(e);
        } else if(label.equals("=")){
        	cal(exp);
        	if(operateValidFlag) {
        		resultText.setText(ans);
        	} else {
        		resultText.setText("除数不能为0");
        	}
        	operateValidFlag = true;
			exp = "";
        } else if(label.equals("sqrt") || label.equals("%") || label.equals("1/x") || label.equals("+/-")){
        	handleSingleOp(label);
        	if(operateValidFlag) {
        		resultText.setText(ans);
        	}
        	operateValidFlag = true;
        }else {
        	JButton btn = ((JButton)e.getSource());
    		exp += btn.getText();
    		resultText.setText(exp);
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
            exp = text;
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
     * 处理C键被按下的事件
     */
    private void handleC() {
        // 初始化计算器的各种值
        resultText.setText("0");
        firstDigit = true;
        exp = "";
        operator = "=";
    }
 
    /**
     *	处理单个运算符的情况
     */
    private void handleSingleOp(String op)
    {
    	operateValidFlag = true;
    	if(op.equals("sqrt")) {
    		double num = Math.sqrt(new BigDecimal(resultText.getText()).doubleValue());
    		ans = String.valueOf(num);
    	} else if(op.equals("%")) {
    		BigDecimal temp = new BigDecimal(resultText.getText());
    		temp = temp.divide(new BigDecimal(100));
    		ans = String.valueOf(temp);
    	} else if(op.equals("1/x")) {
    		BigDecimal temp = new BigDecimal(resultText.getText());
    		if (temp.equals(new BigDecimal("0"))) {
                // 操作不合法
                resultText.setText("零没有倒数");
                operateValidFlag = false;
            } else {
            	temp = new BigDecimal("1").divide(temp);
        		ans = String.valueOf(temp);
            }
    	} else if(op.equals("+/-")) {
    		BigDecimal temp = new BigDecimal(resultText.getText());
    		temp = temp.multiply(new BigDecimal("-1"));
    		ans = String.valueOf(temp);
    	}
    }
    
 // 计算表达式
 	public static void cal(String exp) {
 		if("".equals(exp)) { resultText.setText("0"); ans = "0"; return;}; // 如果表达式是空
 		
 		Stack<BigDecimal> numStack = new Stack<BigDecimal>(); // 数字栈
 		
 		Stack<Character> operatorStack = new Stack<Character>(); // 运算符栈	
 		
 		String tmpNum = ""; // 记录数据
 		
 		exp += "@"; // 在表达式尾部加入结束标志
 		int len = exp.length(); // 表达式的长度
 		int begin = 0; // 循环的下标
 		char c = exp.charAt(0); // 拿表达式的第一个字符
 		
 		operatorStack.push('@'); // 把开始标志加入运算符栈
 		boolean flag = true; // 表达式遍历晚的标志
 		while (c != '@' || !operatorStack.peek().equals('@') && flag) {
 			if(!isOperator(c)) { // 是数字
 				while (!isOperator((c = exp.charAt(begin)))) { // 循环拿出表达式中的数字
 					tmpNum += c;
 					begin++;
 					if(begin == len) {flag = false; break;}
 				}
 				numStack.push(new BigDecimal(tmpNum)); // 把字符串变成一个大数字压入栈
 				tmpNum = ""; // 置空该字符串
 				if(begin == len) break;
 				c = exp.charAt(begin); // 读下一个字符,一定是运算符
 			}
 			else { // 是运算符
 				switch (precede(operatorStack.peek(), c)) { // 比较运算符优先级
 				case '<': // 栈底运算符优先级低
 					operatorStack.push(c); // 运算符入栈
 					c = exp.charAt(++begin); // 读下一个字符
 					break;
 				case '=':
 					operatorStack.pop(); // 退掉括号
 					c = exp.charAt(++begin); // 读下一个字符
 					break;
 				case '>': // 栈底运算符优先级高
 					// 计算
 					numStack.push(Count(numStack.pop(), operatorStack.pop(),numStack.pop()));
 					if(!operateValidFlag) {
 						return;
 					}
 					break;
 				} // switch
 			} // else
 		} 
 		ans = numStack.pop().toString(); // 返回答案
 	}
 	
 	
 	// 计算
 	public static BigDecimal Count(BigDecimal a, Character opera, BigDecimal b) {
 		BigDecimal res = new BigDecimal("0");
 		switch (opera) { // 计算 加 减 乘 除
 		case '+':
 			res = a.add(b);
 			break;
 		case '-':
 			res = b.subtract(a);
 			break;
 		case '*':
 			res = a.multiply(b);
 			break;
 		case '/':
 			if(!a.equals(new BigDecimal("0"))) {
 				res = b.divide(a);
 			} else {
 				operateValidFlag = false;
 			}
 			break;
 		default:
 			break;
 		}
 		return res;
 	}

 	// 判断字符是否是运算符
 	public static boolean isOperator(char c) {
 		 return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '@';
 	}
 	
 	// 返回运算符的优先级
 	public static char precede(char a, char b) {
 		String op = "+-*/()@"; // 运算符
 		char[][] pri= { // 优先级表 // '+','-','*','/','(',')','@'
 				{'>','>','<','<','<','>','>'},
 				{'>','>','<','<','<','>','>'},
 				{'>','>','>','>','<','>','>'},
 				{'>','>','>','>','<','>','>'},
 				{'<','<','<','<','<','=',' '},
 				{'>','>','>','>',' ','>','>'},
 				{'<','<','<','<','<',' ','='}
 		};
 		return pri[op.indexOf(a)][op.indexOf(b)]; // f返回优先级
 	}
 	
	public static void main(String[] args) {
		EventQueue.invokeLater(()->
		{
			calculatorTemp calculator1 = new calculatorTemp();
	        calculator1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        calculator1.setVisible(true);
		});
	}
}
