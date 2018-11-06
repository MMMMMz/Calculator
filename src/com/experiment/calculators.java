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
			}; //�������ϵ����а���
	private final String[] COMMAND = {
			"Backspace", "C","����" ,"��������"
			};	//�������ϵĹ��ܼ�
	private JButton keys[] = new JButton[KEYS.length];	//�������ϵİ�ť
	private JButton commands[] = new JButton[COMMAND.length];	//�������ϵĹ��ܼ�
	private JTextField resultText = new JTextField("0",50);	//�������ı���
	
	// ��־�û������Ƿ����������ʽ�ĵ�һ������,�������������ĵ�һ������
    private boolean firstDigit = true;
    // ������м�����
    private double resultNum = 0.0;
    // ��ǰ����������
    private String operator = "=";
    // �����Ƿ�Ϸ�
    private boolean operateValidFlag = true;
	
    private JPanel panel1 = new JPanel();	//���Ĵ�Ļ���
    
    JPanel commandsPanel = new JPanel();	//��ʼ�����ܼ�
    
    JPanel calckeysPanel = new JPanel();	//��ʼ���������ϵİ���,����һ��������
    
    Stack<Double> numStack = new Stack<Double>(); //����ջ
    
	Stack<String> operatorStack = new Stack<String>(); // �����ջ		
	
	public calculators()	//���캯��
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
            resultText.setText("0");
        } else if(label.equals(COMMAND[2])){
        	//�û����˸ı���ɫ��
        	handlechangecolor(e);
        }else if(label.equals(COMMAND[3])){
        	//�û����˸ı���ɫ��
        	handlechangetheme(e);
        }else if ("0123456789.".indexOf(label) >= 0) {
            // �û��������ּ�����С�����
            handleNumber(label);
        } else {
            // �û������������
            handleOperator(label);
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
     * �������ּ������µ��¼�
     *
     * @param key
     */
    private void handleNumber(String key) {
        if (firstDigit) {
            // ����ĵ�һ������
            resultText.setText(key);
            numStack.push(Double.valueOf(resultText.getText()).doubleValue());	//����Ĳ������������ͽ���ѹ��ջ��
         
        } else if ((key.equals(".")) && (resultText.getText().indexOf(".") < 0)) {
            // �������С���㣬����֮ǰû��С���㣬��С���㸽�ڽ���ı���ĺ���
            resultText.setText(resultText.getText() + ".");		//����Ǹ�С��,�ͽ�֮ǰ��������ջ
            numStack.pop();
        } else if (!key.equals(".")) {
            // �������Ĳ���С���㣬�����ָ��ڽ���ı���ĺ���
            resultText.setText(resultText.getText() + key);
            numStack.push(Double.valueOf(resultText.getText()).doubleValue());	
        }
        // �Ժ�����Ŀ϶����ǵ�һ��������
        firstDigit = false;
    }
 
    /**
     * ����C�������µ��¼�
     */
    private void handleC() {
        // ��ʼ���������ĸ���ֵ
        resultText.setText("0");
        firstDigit = true;
        operator = "=";
    }
 
    /**
     * ����������������µ��¼�
     *
     * @param key
     */
    private void handleOperator(String key) {
        if (operator.equals("/")) {
            // ��������
            // �����ǰ����ı����е�ֵ����0
            if (getNumberFromText() == 0.0) {
                // �������Ϸ�
                operateValidFlag = false;
                resultText.setText("��������Ϊ��");
            } else {
                resultNum /= getNumberFromText();
            }
        } else if (operator.equals("1/x")) {
            // ��������
            if (resultNum == 0.0) {
                // �������Ϸ�
                operateValidFlag = false;
                resultText.setText("��û�е���");
            } else {
                resultNum = 1 / resultNum;
            }
        } else if (operator.equals("+")) {
            // �ӷ�����
            resultNum += getNumberFromText();
        } else if (operator.equals("-")) {
            // ��������
            resultNum -= getNumberFromText();
        } else if (operator.equals("*")) {
            // �˷�����
            resultNum *= getNumberFromText();
        } else if (operator.equals("sqrt")) {
            // ƽ��������
            resultNum = Math.sqrt(resultNum);
        } else if (operator.equals("%")) {
            // �ٷֺ����㣬����100
            resultNum = resultNum / 100;
        } else if (operator.equals("+/-")) {
            // ������������
            resultNum = -resultNum;
        } else if (operator.equals("=")) {
            // ��ֵ����
            resultNum = getNumberFromText();
        }
        if (operateValidFlag) {	//�������������ʾ����,����������ʾ������
            // ˫���ȸ�����������
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
        // ����������û����İ�ť
        operator = key;
        operatorStack.push(operator);
        firstDigit = true;
        operateValidFlag = true;
    }
	
    /**
     * �����ı����л�ȡ���ַ���ת��Ϊ������
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
