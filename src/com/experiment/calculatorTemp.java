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
			}; //�������ϵ����а���
	private final String[] COMMAND = {
			"Backspace", "C","����","��������"
			};	//�������ϵĹ��ܼ�
	private JButton keys[] = new JButton[KEYS.length];	//�������ϵİ�ť
	private JButton commands[] = new JButton[COMMAND.length];	//�������ϵĹ��ܼ�
	private static JTextField resultText = new JTextField("0",50);	//�������ı���
	
	// ��־�û������Ƿ����������ʽ�ĵ�һ������,�������������ĵ�һ������
    private boolean firstDigit = true;
    // ������м�����
    private BigDecimal resultNum = new BigDecimal(0);
    // ��ǰ����������
    private String operator = "=";
    // �����Ƿ�Ϸ�
    private static boolean operateValidFlag = true;
	
    private JPanel panel1 = new JPanel();	//���Ĵ�Ļ���
    
    JPanel commandsPanel = new JPanel();	//��ʼ�����ܼ�
    
    JPanel calckeysPanel = new JPanel();	//��ʼ���������ϵİ���,����һ��������
	
	String exp = "";
	
	private static String ans = "0"; // ������Ľ��
	
	public calculatorTemp()	//���캯��
	{
		init();
		this.setTitle("MaZhe�ļ�����");	//���ü������ı���
		this.setBackground(Color.BLACK);	//���ñ�����ɫ
		this.setSize(500, 500);
		this.setLocation(500, 300);	//���ü���������Ļ�ϵ�λ��
		this.setResizable(false);	//���ü������Ĵ�С�����޸�
		setLocationByPlatform(true);	//�����ڳ��ָ���ƽ̨ϰ��
		String className = "javax.swing.plaf.nimbus.NimbusLookAndFeel";	//�����۸�
		try { 
			UIManager.setLookAndFeel(className); SwingUtilities.updateComponentTreeUI(this);
		} catch(Exception e){
			e.printStackTrace();
		}
//		this.pack();//�Զ����������С
	}
	
	public void init()	//��ʼ������
	{
		resultText.setHorizontalAlignment(JTextField.RIGHT);	//�����ı�������Ϊ�Ҷ���
		resultText.setEditable(false);
		resultText.setFont(new Font("Monospaced", Font.PLAIN, 35));
		resultText.setBackground(Color.WHITE);	//�����ı���ı�����ɫΪ��ɫ
		
		calckeysPanel.setLayout(new GridLayout(4, 5, 5, 5));	//���ð�������Ϊ���񲼾�,Ϊ��������
		for(int i = 0 ; i < KEYS.length ; i++)
		{
			keys[i] = new JButton(KEYS[i]);	//ѭ����Ӱ�ť
			keys[i].setFont(new Font("Monospaced", Font.PLAIN, 20));	//����ÿһ����ť������
			calckeysPanel.add(keys[i]);	//����ť��ӵ�������
			keys[i].setForeground(Color.WHITE);	//���������ϵİ�������Ϊ��ɫ
			keys[i].setBackground(Color.DARK_GRAY);	//�������ı�����ɫ����Ϊ���ɫ
		}
		// ��������ı�����ɫΪ��ɫ
        keys[3].setForeground(Color.ORANGE);
        keys[8].setForeground(Color.ORANGE);
        keys[13].setForeground(Color.ORANGE);
        keys[18].setForeground(Color.ORANGE);
        keys[19].setForeground(Color.ORANGE);
        
        
        commandsPanel.setLayout(new GridLayout(1, 3, 4, 4));	//���ù��ܼ�����Ϊ����ʽ����,һ������
        for (int i = 0; i < COMMAND.length; i++) {
            commands[i] = new JButton(COMMAND[i]);
            commands[i].setFont(new Font("Monospaced", Font.PLAIN, 20));
            commandsPanel.add(commands[i]);
            commands[i].setForeground(Color.BLACK);
            commands[i].setBackground(Color.LIGHT_GRAY);
        }
        
        // ������м����������岼�֣���calckeys��command������ڼ��������в���

        // �½�һ����Ļ��壬�����潨����command��calckeys������ڸû�����
 
        // ������ñ߽粼�ֹ����������������֮���ˮƽ�ʹ�ֱ�����ϼ����Ϊ4����
        panel1.setLayout(new BorderLayout(4, 4));
        panel1.add("North", commandsPanel);
        panel1.add("Center", calckeysPanel);
        
        //����һ���������ı���
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add("Center", resultText);
        
        // ���岼��
        getContentPane().setLayout(new BorderLayout(3, 5));
        getContentPane().add("North", top);
        getContentPane().add("Center", panel1);
        
        // Ϊ����ť����¼�������
        // ��ʹ��ͬһ���¼�����������������
        for (int i = 0; i < KEYS.length; i++) {
            keys[i].addActionListener(this);
        }
        for (int i = 0; i < COMMAND.length; i++) {
            commands[i].addActionListener(this);
        }
	}
	
	
	 /**
     * �����¼�
     */
    public void actionPerformed(ActionEvent e) {
        // ��ȡ�¼�Դ�ı�ǩ
        String label = e.getActionCommand();
        if (label.equals(COMMAND[0])) {
            // �û�����"Backspace"��
            handleBackspace();
        } else if (label.equals(COMMAND[1])) {
            // �û�����"C"��
        	handleC();
        } else if(label.equals(COMMAND[2])){
        	//�û����˸ı���ɫ��
        	handlechangecolor(e);
        }else if(label.equals(COMMAND[3])){
        	//�û����˸ı���ɫ��
        	handlechangetheme(e);
        } else if(label.equals("=")){
        	cal(exp);
        	if(operateValidFlag) {
        		resultText.setText(ans);
        	} else {
        		resultText.setText("��������Ϊ0");
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
     * ����Backspace�������µ��¼�
     */
    private void handleBackspace() {
        String text = resultText.getText();
        int i = text.length();
        if (i > 0) {
            // �˸񣬽��ı����һ���ַ�ȥ��
            text = text.substring(0, i - 1);
            exp = text;
            if (text.length() == 0) {
                // ����ı�û�������ݣ����ʼ���������ĸ���ֵ
                resultText.setText("0");
                firstDigit = true;
                operator = "=";
            } else {
                // ��ʾ�µ��ı�
                resultText.setText(text);
            }
        }
    }
    
    /**
     * �����������
     * @param e
     */
    private void handlechangetheme(ActionEvent e) {
    	// �г���װ�����й۸�
    		LookAndFeelInfo[] looksinfo = UIManager.getInstalledLookAndFeels();
    	// ��ȡ�۸�����������
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
     * 	�������������ɫ
     */
    private void handlechangecolor(ActionEvent e) {
    	int num1 = new Random().nextInt(255);	//��ȡ����������������ȡһ����ɫ
    	int num2 = new Random().nextInt(255);
    	int num3 = new Random().nextInt(255);
    	Color c = new Color(num1,num2,num3);
    	panel1.setBackground(c);
    	commandsPanel.setBackground(c);
    	calckeysPanel.setBackground(c);
    }
 
    /**
     * ����C�������µ��¼�
     */
    private void handleC() {
        // ��ʼ���������ĸ���ֵ
        resultText.setText("0");
        firstDigit = true;
        exp = "";
        operator = "=";
    }
 
    /**
     *	����������������
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
                // �������Ϸ�
                resultText.setText("��û�е���");
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
    
 // ������ʽ
 	public static void cal(String exp) {
 		if("".equals(exp)) { resultText.setText("0"); ans = "0"; return;}; // ������ʽ�ǿ�
 		
 		Stack<BigDecimal> numStack = new Stack<BigDecimal>(); // ����ջ
 		
 		Stack<Character> operatorStack = new Stack<Character>(); // �����ջ	
 		
 		String tmpNum = ""; // ��¼����
 		
 		exp += "@"; // �ڱ��ʽβ�����������־
 		int len = exp.length(); // ���ʽ�ĳ���
 		int begin = 0; // ѭ�����±�
 		char c = exp.charAt(0); // �ñ��ʽ�ĵ�һ���ַ�
 		
 		operatorStack.push('@'); // �ѿ�ʼ��־���������ջ
 		boolean flag = true; // ���ʽ������ı�־
 		while (c != '@' || !operatorStack.peek().equals('@') && flag) {
 			if(!isOperator(c)) { // ������
 				while (!isOperator((c = exp.charAt(begin)))) { // ѭ���ó����ʽ�е�����
 					tmpNum += c;
 					begin++;
 					if(begin == len) {flag = false; break;}
 				}
 				numStack.push(new BigDecimal(tmpNum)); // ���ַ������һ��������ѹ��ջ
 				tmpNum = ""; // �ÿո��ַ���
 				if(begin == len) break;
 				c = exp.charAt(begin); // ����һ���ַ�,һ���������
 			}
 			else { // �������
 				switch (precede(operatorStack.peek(), c)) { // �Ƚ���������ȼ�
 				case '<': // ջ����������ȼ���
 					operatorStack.push(c); // �������ջ
 					c = exp.charAt(++begin); // ����һ���ַ�
 					break;
 				case '=':
 					operatorStack.pop(); // �˵�����
 					c = exp.charAt(++begin); // ����һ���ַ�
 					break;
 				case '>': // ջ����������ȼ���
 					// ����
 					numStack.push(Count(numStack.pop(), operatorStack.pop(),numStack.pop()));
 					if(!operateValidFlag) {
 						return;
 					}
 					break;
 				} // switch
 			} // else
 		} 
 		ans = numStack.pop().toString(); // ���ش�
 	}
 	
 	
 	// ����
 	public static BigDecimal Count(BigDecimal a, Character opera, BigDecimal b) {
 		BigDecimal res = new BigDecimal("0");
 		switch (opera) { // ���� �� �� �� ��
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

 	// �ж��ַ��Ƿ��������
 	public static boolean isOperator(char c) {
 		 return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '@';
 	}
 	
 	// ��������������ȼ�
 	public static char precede(char a, char b) {
 		String op = "+-*/()@"; // �����
 		char[][] pri= { // ���ȼ��� // '+','-','*','/','(',')','@'
 				{'>','>','<','<','<','>','>'},
 				{'>','>','<','<','<','>','>'},
 				{'>','>','>','>','<','>','>'},
 				{'>','>','>','>','<','>','>'},
 				{'<','<','<','<','<','=',' '},
 				{'>','>','>','>',' ','>','>'},
 				{'<','<','<','<','<',' ','='}
 		};
 		return pri[op.indexOf(a)][op.indexOf(b)]; // f�������ȼ�
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
