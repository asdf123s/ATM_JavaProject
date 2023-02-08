package atm.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import atm.account.Account;
import atm.deal.Deal;
import atm.managerService.ATMCash;

public class Data {

	private final static String ACCOUNT;
	   private final static String DEAL;
	   private final static String ATMCASH;
	   
	   public static ArrayList<Account> account; //= account.txt
	   public static ArrayList<Deal> deal;   //= deal.tct
	   public static ArrayList<ATMCash> atmcash;   //= ATMCash.txt
	   
	   static {
	      
	      ACCOUNT = ".\\dat\\account.txt";
	      DEAL = ".\\dat\\deal.txt"; 
	      ATMCASH = ".\\dat\\ATMCash.txt";
	      
	      account = new ArrayList<Account>();
	      deal = new ArrayList<Deal>();
	      atmcash = new ArrayList<ATMCash>();
	   }
	   
	   public static int createAccNum() {
	      
	      int s1 = account.get(account.size() -1).getAccountNumber() + 1;
	     
	      
	      
	      return s1;
	   }
	   
	public static ArrayList<Deal> listDeal(int accountNumber , String name, boolean stop){
	      
	      //account.txt(계좌번호, 이름, 정지유무) -> deal.txt
	      
	      ArrayList<Deal> list = new ArrayList<Deal>();
	      
	      for(Deal d : deal) {
	         
	         if(d.getAccountNumber() == accountNumber) {
	            
	            list.add(d);
	         }
	      }
	      
	      return list;
	   }
	   

	public static void load() {
	      
	      //텍스트 파일 > 컬렉션
	      
	      try {
	         BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT));
	         
	         String line = null;
	         
	         while((line = reader.readLine())!= null) {
	            
	            
	            String[] temp = line.split(",");
	            
	            
	            Account acc = new Account();
	            
	            //계좌번호, 가입일, PW, 이름, 생년월일, 전화번호, 주민등록번호, 정지유무(true, false), ID
	            acc.setAccountNumber(Integer.parseInt(temp[0]));
	            acc.setJoinDay(temp[1]);
	            acc.setPW(temp[2]);
	            acc.setName(temp[3]);
	            acc.setBirth(temp[4]);
	            acc.setTel(temp[5]);
	            acc.setJoomin(temp[6]);
	            acc.setStop(Boolean.valueOf(temp[7]));
	            acc.setID(temp[8]);
	            acc.setBalance(Integer.parseInt(temp[9]));
	            
	            account.add(acc);
	            
	            
	         }//while
	         
	         reader.close();
	         
	         
	         reader = new BufferedReader(new FileReader(DEAL));
	         
	         line = null;
	         
	         while((line = reader.readLine())!= null) {
	            
	            //계좌번호, 거래일, 송신 이름, 수신 이름, 입금금액, 출금금액, 거래후잔액
	            
	            String[] temp = line.split(",");
	            
	            Deal dl = new Deal();
	            
	            dl.setAccountNumber(Integer.parseInt(temp[0]));
	            dl.setTradingDay(temp[1]);
	            dl.setTransName(temp[2]);
	            dl.setReceiveName(temp[3]);
	            dl.setDeposit(Integer.parseInt(temp[4]));
	            dl.setWithdrawal(Integer.parseInt(temp[5]));
	            dl.setCashmoney(Integer.parseInt(temp[6]));  
	            deal.add(dl);

	         }


	         reader.close();
	         
	         
	         reader = new BufferedReader(new FileReader(ATMCASH));
	         
	         line = null;
	         
	         while((line = reader.readLine())!= null) {
	            
	            //숫자, 잔액
	            
	            String[] temp = line.split(",");
	            
	            ATMCash atc = new ATMCash();
	            
	            atc.setNumber(Integer.parseInt(temp[0]));
	            atc.setCash(Integer.parseInt(temp[1]));
	         
	            atmcash.add(atc);

	         }
	         
	         reader.close();
	         
	      } catch (Exception e) {
	         System.out.println("Data.load");
	         e.printStackTrace();
	      }
	   }//load


	   public static void save() {
	      // 컬렉션 > 텍스트 파일
	   
	      try {
	   
	         //계좌번호, 가입일, PW, 이름, 생년월일, 전화번호, 주민등록번호, 정지유무(true, false), ID
	         BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT));
	   
	         for (Account acc : account) {
	            String data = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%d"
	                                 , acc.getAccountNumber()
	                                 , acc.getJoinDay()
	                                 , acc.getPW()
	                                 , acc.getName()
	                                 , acc.getBirth()
	                                 , acc.getTel()
	                                 , acc.getJoomin()
	                                 , acc.isStop()
	                                 , acc.getID()
	                                 , acc.getBalance());
	   
	            writer.write(data + "\r\n");
	         }
	         writer.close();
	         
	         //계좌번호, 거래일, 송신 이름, 수신 이름, 입금금액, 출금금액, 거래후잔액
	         writer = new BufferedWriter(new FileWriter(DEAL));
	         
	         for(Deal dl : deal) {
	            String data = String.format("%s,%s,%s,%s,%d,%d,%d"
	                                 , dl.getAccountNumber()
	                                 , dl.getTradingDay()
	                                 , dl.getTransName()
	                                 , dl.getReceiveName()
	                                 , dl.getDeposit()
	                                 , dl.getWithdrawal()
	            					 , dl.getCashmoney());
	            	
	            writer.write(data + "\r\n");
	         }
	         writer.close();
	         
	         //숫자, 금액
	         writer = new BufferedWriter(new FileWriter(ATMCASH));
	         
	         for(ATMCash atc : atmcash) {
	            String data = String.format("%d,%d", atc.getNumber(), atc.getCash());
	            
	            writer.write(data + "\r\n");
	         }
	         writer.close();
	         
	         
	      } catch (Exception e) {
	         System.out.println("Data.save");
	         e.printStackTrace();
	      }
	   }//save
	   
	   public static Account getAccount(int acc) {
	       
	       for (Account a : account) {
	          
	          if (a.getAccountNumber() == acc) {
	             
	             return a;
	          }
	          
	       }
	       
	       return null;
	    }

	   //일시 정지
	      public static void pause() {
	         
	         System.out.println();
	         System.out.println("계속하시려면 엔터를 입력하세요.");
	         
	         Scanner scan = new Scanner(System.in);
	         
	         scan.nextLine();
	         System.out.println();
	      }

	      public static ArrayList<Account> findPw(String name, String id, String jumin) {
	    	  
	         ArrayList<Account> acclist = new ArrayList<Account>();

	         for (Account acc : account) {

	            if (acc.getName().contains(name) && acc.getID().contains(id) && acc.getJoomin().contains(jumin)) {

	               acclist.add(acc);
	            }

	         }

	         return acclist;

	      }
//	      public static void getAccNum(int accountNumber, String name) {
//	         for(Account acc : account) {
//	            if(acc.getAccountNumber() == accountNumber && acc.getName().equals(name)) {
//	               return; 
//	            }
//	         }
//	      }
	      


	   public static boolean deleteAcc(int accountNumber ,String name) {
	      
	      
	      for(int i=0; i<account.size(); i++) {
	         if(account.get(i).getAccountNumber()== accountNumber && account.get(i).getName().equals(name)) {
	            account.remove(i);
	         }
	      }
	      
	      return false;
	      
	   }

	public static int createCashNum() {
		
		int n = atmcash.get(atmcash.size() -1).getNumber() + 1;
		
	    return n;
	    
	}

	public static ArrayList<Account> findId(String name, String jumin) {
		 ArrayList<Account> idlist = new ArrayList<Account>();

         for (Account acc : account) {

            if (acc.getName().contains(name) && acc.getJoomin().contains(jumin)) {

               idlist.add(acc);
            }

         }

         return idlist;

      }

	public static void deleteDeal(int input1) {
		ArrayList<Deal> dlist = new ArrayList<Deal>();
		
		 for (Deal de : deal) {

	            if (de.getAccountNumber() != input1) {

	               dlist.add(de);
	            }

	         }

	         deal = dlist;
	} 
	
	
	}


	   	   