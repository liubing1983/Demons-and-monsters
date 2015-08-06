package com.lb.stack;

import java.util.Stack;

import org.apache.commons.lang.StringUtils;

public class Postfix {

	public static void main(String[] args) {

		// 拆分中缀表达式
		// String[] s = "11+2*3/9".split("[+\\-\\*/]");
		// 中缀转后缀
		// infixToSuffix("1 * 11 + 2 * 9 / ( 3 － 2 )".split(" "));
		// String s = "1 * 11 + 2 * 9 / ( 3 - 2 )";
		String s = "1 + 1 + 3 + 5 * 7 - 2 * ( 5 + 2 )";
		if (args.length > 0) {
			s = args[0];
		}
		System.out.println("@@@@@@@@@@@@@@@@@   原始中缀表达式: " + s);
		System.out.println("结果: " + count(infixToSuffix(splits(s))));
	}

	/**
	 * 按固定分隔符分割表达式
	 * 
	 * @param infix
	 * @return
	 */
	public static String[] splits(String infix) {
		StringBuffer sb = new StringBuffer();
		StringBuffer split_infix = new StringBuffer();
		String[] s = infix.split("");
		for (String ss : s) {
			// 如果是空格跳过
			if (StringUtils.isBlank(ss)) {
				continue;
			}
			// 判断是否式符号
			if (!ss.equals("+") && !ss.equals("-") && !ss.equals("*") && !ss.equals("/") && !ss.equals("(") && !ss.equals(")")) {
				// 不是符号, 拼接数字
				sb.append(ss);
			} else {
				// 是符号, 按固定分割符, 拼接子符串
				split_infix.append(sb).append(" ").append(ss).append(" ");
				sb = new StringBuffer();
			}

		}
		String lb = split_infix.append(sb).toString();
		System.out.println("@@@@@@@@@@@@@@@@@   转换后的中缀表达式: " + lb);
		return lb.split(" ");
	}

	/**
	 * 中缀表达式转换后缀表达式
	 * 
	 * @param infix
	 * @return
	 */
	public static String[] infixToSuffix(String[] infix) {
		// 后缀表达式
		StringBuffer sb = new StringBuffer();

		Stack<String> stack = new Stack<String>();

		// 遍历所有数据
		for (String ss : infix) {
			System.out.println("字符:" + ss + ", 后缀表达式: [" + sb.toString() + "], 栈:" + stack.toString());
			// 判断是否是空格
			if (StringUtils.isNotEmpty(ss)) {
				// 判断是符号还是数字
				if (StringUtils.isNumeric(ss)) {
					System.out.println("数字直接拼接字符串：" + ss);
					sb.append(ss).append(" ");
				} else {
					// 判断栈里是否存在符号
					if (stack.isEmpty() || ss.equals("(")) {
						// 栈为空， 将符号放进栈中
						System.out.println("栈为空，　直接入栈:" + ss);
						stack.push(ss);
					} else {
						// 判断是否是")"
						if (ss.equals(")")) {
							System.out.println("发现 ')', 从栈中依次取出数据, 直到发现 '('  :" + stack.toString());
							while (true) {
								// System.out.println("===" + stack.peek());
								if (stack.peek().equals("(")) {
									System.out.println("栈中数据是'(', 出栈");
									stack.pop();
									break;
								} else {
									System.out.println("栈中数据不是'(', 出栈拼接后缀表达式, " + stack.peek());
									sb.append(stack.pop()).append(" ");
								}
							}
							break;
						}
						// 栈不为空， 比较等级
						System.out.println("当前符号:" + ss + ",级别:" + level(ss) + ";  栈顶符号" + stack.peek() + ", 级别:" + level(stack.peek()));
						if (level(ss) < level(stack.peek())) {
							System.out.println("当前符号级别小于栈顶级别, 栈中符号依次出栈.");
							while (level(ss) <= level(stack.peek())) {
								sb.append(stack.pop()).append(" ");
								if (stack.isEmpty())
									break;
							}

						}
						System.out.println("比较等级，　入栈:" + ss);
						stack.push(ss);

					}
				}
			}
		}
		System.out.println("表达式遍历结束, 将栈中数据依次拼接到后缀表达式中." + stack.toString());
		while (!stack.isEmpty()) {
			sb.append(stack.pop()).append(" ");
		}

		System.out.println("@@@@@@@@@@@@@@@@@   后缀表达式:"+sb.toString());
		return sb.toString().split(" ");
	}

	/**
	 * 判断标点符号等级
	 * 
	 * @param s
	 * @return
	 */
	public static int level(String s) {
		if (s.equals("+") || s.equals("-")) {
			return 1;
		} else if (s.equals("(")) {
			return 0;
		} else {
			return 2;
		}
	}

	/**
	 * 计算总数
	 * 
	 * @param suffix
	 * @return
	 */
	public static String count(String[] suffix) {
		System.out.println("%%%%%%%%%%%%%%%%%%%%  后缀表达式转换完成, 开始计算结果.  %%%%%%%%%%%%%%%%%%%%%");
		Stack<Integer> stack = new Stack<Integer>();
		for (String s : suffix) {
			System.out.println("符号:" + s + "---------------" + stack.toString());
			if (StringUtils.isNumeric(s)) {
				System.out.println("数字入栈：" + s);
				stack.push(Integer.valueOf(s));
			} else {
				switch (s.trim()) {
				case "+":
					int a1 = stack.pop();
					int b1 = stack.pop();
					System.out.println("计算: " + a1 + "+" + b1);
					stack.push(b1 + a1);
					break;
				case "-":
					int a2 = stack.pop();
					int b2 = stack.pop();
					System.out.println("计算: " + b2 + "-" + a2);
					stack.push(b2 - a2);
					break;
				case "*":
					int a3 = stack.pop();
					int b3 = stack.pop();
					System.out.println("计算: " + b3 + "*" + a3);
					stack.push(b3 * a3);
					break;
				case "/":
					int a4 = stack.pop();
					int b4 = stack.pop();
					System.out.println("计算: " + a4 + "/" + b4);
					stack.push(b4 / a4);
					break;
				default:
					break;
				}
			}
		}
		return stack.pop().toString();
	}

}
