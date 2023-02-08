package atm.atmService;

import java.util.Calendar;
import java.util.Scanner;

import atm.account.Account;
import atm.data.Data;
import atm.deal.Deal;
import atm.managerService.ATMCash;

public class Atm {

	public static void atmProgram(Account id) {

		boolean loop = true;

		while (loop) { // 화면 시작
			int atmAmount = Data.atmcash.get(Data.atmcash.size() - 1).getCash();
			Scanner scan = new Scanner(System.in);
			
			System.out.println();
			System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
			System.out.println("┃            *****계좌 관리 프로그램*****           ");
			System.out.println("┃==========                            =========");
			System.out.println("┃1.예금조회      ┏━━━━━━━━━━━━━━━━━┓      4.계좌이체");
			System.out.println("┃=========     ┃ 입금 ┃ 출금 ┃ 명세 ┃     =========");
			System.out.println("┃2.예금출금      ┃━━━━━━━━━━━━━━━━━┃      5.내역조회");
			System.out.printf("┃=========     ┃  O  ┃  %s  ┃  O  ┃     =========\n", (atmAmount >= 1000000 ? "O" : "X"));
			System.out.println("┃3.입금         ┗━━━━━━━━━━━━━━━━━┛         6.종료");
			System.out.println("┃=========			       =========");
			System.out.println("┃				   	   ");
			System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.print("선택: ");
			String select = scan.nextLine();
			System.out.println();
			
			if (select.equals("1")) {// 조회
				view(id);
			} else if (select.equals("2")) { // 예금 출금
				chul(id, scan); // 출금
			} else if (select.equals("3")) { // 입금
				ib(id, scan);
			} else if (select.equals("4")) { // 계좌이체
				iche(id, scan);
			} // 계좌이체
			else if (select.equals("5")) {
				history(id);
			} else if (select.equals("6")) {
				loop = false;
			}
		}
	}

	private static void view(Account id) {
		int balance = 0;
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃                                       *예금조회 ");
		System.out.printf("┃계좌번호: %s\n", id.getAccountNumber());
		System.out.printf("┃이름: %s\n", id.getName());
		System.out.printf("┃잔액: %,d원\n", id.getBalance());// 예금조회
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");// 조회
		Data.pause();

	}

	private static void chul(Account id, Scanner scan) {

		if (id.isStop() == false) {
			System.out.println("*거래가 중지된 계좌입니다. 관리자에게 문의하시기 바랍니다.");
			System.out.println();
			Data.pause();
			return;
		}
		int atmAmount = Data.atmcash.get(Data.atmcash.size() - 1).getCash();
		int balance = 0;
		int chul = 0;
		balance = id.getBalance();
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃                                       *예금출금 ");
		System.out.print("┃출금 금액: ");
		chul = scan.nextInt();
		scan.nextLine();
		atmAmount -= chul;
		if (balance >= chul && atmAmount >= chul) {
			id.setBalance(balance - chul);
			System.out.printf("┃%,d원 출금 완료되었습니다.\n", chul);
			System.out.printf("┃잔액: %,d원\n", id.getBalance());
			System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			
			// 계좌번호, 거래일, 송신 이름, 수신 이름, 입금금액, 출금금액, 거래후잔액
			Deal dealSave = new Deal();
			dealSave.setAccountNumber(id.getAccountNumber());
			Calendar c = Calendar.getInstance();
			dealSave.setTradingDay(String.format("%tF", c));
			dealSave.setTransName(id.getName());
			dealSave.setReceiveName("ATM");
			dealSave.setDeposit(0);
			dealSave.setWithdrawal(chul);
			dealSave.setCashmoney(id.getBalance());
			Data.deal.add(dealSave);
			ATMCash cashSave = new ATMCash();
			int num = Data.createCashNum();
			cashSave.setNumber(num);
			cashSave.setCash(atmAmount);
			Data.atmcash.add(cashSave);
			Data.save();

			System.out.print("명세표를 출력하시겠습니까? (y,n): ");
			String myeongse = scan.nextLine();
			if (myeongse.equals("y")) {
				System.out.println("*명세표를 출력합니다.");

				System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
				System.out.println("┃       명세표");
				System.out.println("┃");
				System.out.printf("┃  거래일:  %s\n", Data.deal.get(Data.deal.size() - 1).getTradingDay());
				System.out.printf("┃  계좌번호:  %d\n", Data.deal.get(Data.deal.size() - 1).getAccountNumber());
				System.out.printf("┃  전화번호:  %s\n", id.getTel());
				System.out.println("┃  입금계좌:  X");
				System.out.println("┃  수취인명: X");
				System.out.printf("┃  거래금액:  %,d원\n", chul);
				System.out.println("┃  수수료: 0원");
				System.out.printf("┃  거래후잔액: %,d원\n", id.getBalance());
				System.out.println("┃");
				System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

			} else {
				System.out.println("*명세표를 출력하지 않습니다.");

			}
			

		} else if (balance < chul) {
			System.out.println("┃계좌의 잔액이 부족합니다.");
			

		} else if (atmAmount < chul) {
			System.out.println("┃기기의 잔액이 부족합니다.");
			
		}
		Data.pause();
	}

	private static void ib(Account id, Scanner scan) {
		if (id.isStop() == false) {
			System.out.println("*거래가 중지된 계좌입니다. 관리자에게 문의하시기 바랍니다.");
			Data.pause();
			return;
		}
		int atmAmount = Data.atmcash.get(Data.atmcash.size() - 1).getCash();
		int balance = 0;
		int ib = 0;
		balance = id.getBalance();
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃                                          *입금 ");
		System.out.print("┃입금 금액: ");
		ib = scan.nextInt();
		scan.nextLine();
		atmAmount += ib;
		id.setBalance(balance + ib);
		System.out.printf("┃%,d원 입금 완료되었습니다.\n", ib);
		System.out.printf("┃잔액: %,d원\n", id.getBalance());
		
		// 계좌번호, 거래일, 송신 이름, 수신 이름, 입금금액, 출금금액, 거래후잔액
		Deal dealSave = new Deal();
		dealSave.setAccountNumber(id.getAccountNumber());
		Calendar c = Calendar.getInstance();
		dealSave.setTradingDay(String.format("%tF", c));
		dealSave.setTransName("ATM");
		dealSave.setReceiveName(id.getName());
		dealSave.setDeposit(ib);
		dealSave.setWithdrawal(0);
		dealSave.setCashmoney(id.getBalance());
		Data.deal.add(dealSave);
		ATMCash cashSave = new ATMCash();
		int num = Data.createCashNum();
		cashSave.setNumber(num);
		cashSave.setCash(atmAmount);
		Data.atmcash.add(cashSave);
		Data.save(); // 입금
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		
		System.out.print("명세표를 출력하시겠습니까? (y,n): ");
		String myeongse = scan.nextLine();
		if (myeongse.equals("y")) {
			System.out.println("*명세표를 출력합니다.");

			System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
			System.out.println("┃       명세표");
			System.out.println("┃");
			System.out.printf("┃  거래일:  %s\n", Data.deal.get(Data.deal.size() - 1).getTradingDay());
			System.out.printf("┃  계좌번호:  %d\n", Data.deal.get(Data.deal.size() - 1).getAccountNumber());
			System.out.printf("┃  전화번호:  %s\n", id.getTel());
			System.out.println("┃  입금계좌:  X");
			System.out.println("┃  수취인명: ATM");
			System.out.printf("┃  거래금액:  %,d원\n", ib);
			System.out.println("┃  수수료: 0원");
			System.out.printf("┃  거래후잔액: %,d원\n", id.getBalance());
			System.out.println("┃");
			System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		} else {
			System.out.println("*명세표를 출력하지 않습니다.");
		}
		Data.pause();

	}

	private static void iche(Account id, Scanner scan) {
		if (id.isStop() == false) {
			System.out.println("*거래가 중지된 계좌입니다. 관리자에게 문의하시기 바랍니다.");
			Data.pause();
			return;
		}
		int acc = 0;
		int iche = 0;
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃                                       *계좌이체 ");
		System.out.print("┃계좌번호: ");
		acc = scan.nextInt();
		for (Account ac : Data.account) {
			if (ac.getAccountNumber() == acc) {
				System.out.print("┃이체 금액: ");
				iche = scan.nextInt();
				scan.nextLine();
				if (iche <= id.getBalance()) {
					id.setBalance(id.getBalance() - iche);
					ac.setBalance(ac.getBalance() + iche);
					System.out.printf("┃%s 계좌로 %,d원 이체 완료되었습니다.\n", ac.getAccountNumber(), iche);
					System.out.printf("┃잔액: %,d원\n", id.getBalance());
					
					Deal dealSave = new Deal();
					dealSave.setAccountNumber(id.getAccountNumber());
					Calendar c = Calendar.getInstance();
					dealSave.setTradingDay(String.format("%tF", c));
					dealSave.setTransName(id.getName());
					dealSave.setReceiveName(ac.getName());
					dealSave.setDeposit(0);
					dealSave.setWithdrawal(iche);
					dealSave.setCashmoney(id.getBalance());
					Data.deal.add(dealSave);

					dealSave = new Deal();
					dealSave.setAccountNumber(ac.getAccountNumber());
					c = Calendar.getInstance();
					dealSave.setTradingDay(String.format("%tF", c));
					dealSave.setTransName(ac.getName());
					dealSave.setReceiveName(id.getName());
					dealSave.setDeposit(iche);
					dealSave.setWithdrawal(0);
					dealSave.setCashmoney(ac.getBalance());
					Data.deal.add(dealSave);
					Data.save(); // 입금
					System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
					System.out.print("명세표를 출력하시겠습니까? (y,n): ");
					String myeongse = scan.nextLine();
					if (myeongse.equals("y")) {
						System.out.println("*명세표를 출력합니다.");

						System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
						System.out.println("┃       명세표");
						System.out.println("┃");
						System.out.printf("┃  거래일:  %s\n", Data.deal.get(Data.deal.size() - 1).getTradingDay());
						System.out.printf("┃  계좌번호:  %d\n", Data.deal.get(Data.deal.size() - 1).getAccountNumber());
						System.out.printf("┃  전화번호:  %s\n", id.getTel());
						System.out.printf("┃  입금계좌:  %d\n", ac.getAccountNumber());
						System.out.printf("┃  수취인명: %s\n", ac.getName());
						System.out.printf("┃  거래금액:  %,d원\n", iche);
						System.out.println("┃  수수료: 0원");
						System.out.printf("┃  거래후잔액: %,d원\n", id.getBalance());
						System.out.println("┃");
						System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

					} else {
						System.out.println("*명세표를 출력하지 않습니다.");

					}
					Data.pause();
					return;

				} else if (iche >= id.getBalance()) {
					System.out.println("┃계좌에 잔액이 부족합니다.");
					System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
					Data.pause();
					return;

				} // 잔액부족
			} // if 있는지
		} // for
		System.out.println("*존재하지 않는 계좌번호 입니다.");
		Data.pause();
	}

	private static void history(Account id) {
		int i = 1;
		if (id.isStop() == false) {
			System.out.println("*거래가 중지된 계좌입니다. 관리자에게 문의하시기 바랍니다.");
			Data.pause();
			return;
		}
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("┃                                       *내역조회 ");
		System.out.printf("┃*****%s님의 내역 조회*****\n", id.getName());
		System.out.println("┃");
		System.out.println("┃===============================================");
		System.out.println("┃[No.] [거래날짜]  [찾으신 금액] [맡기신 금액]    [잔액]");
		System.out.println("┃===============================================");
		for (Deal deal : Data.deal) {
			if (deal.getAccountNumber() == id.getAccountNumber()) {
				if (deal.getDeposit() == 0) {
					if (deal.getReceiveName().equals("ATM")) {
						System.out.printf("┃[%d]  %s %,9d\t%s   %,9d\n", i, deal.getTradingDay(), deal.getWithdrawal(), "ATM ", deal.getCashmoney());
						i++;
					} else {
						System.out.printf("┃[%d]  %s %,9d\t%s   %,9d\n", i, deal.getTradingDay(), deal.getWithdrawal(), deal.getReceiveName(), deal.getCashmoney());
						i++;
					}
				} else if (deal.getWithdrawal() == 0) {
					if (deal.getTransName().equals("ATM")) {
						System.out.printf("┃[%d]  %s %s\t  %,9d    %,9d\n", i, deal.getTradingDay(), "   ATM", deal.getDeposit(), deal.getCashmoney());
						i++;
					} else {
						System.out.printf("┃[%d]  %s   %s\t  %,9d    %,9d\n", i, deal.getTradingDay(), deal.getReceiveName(), deal.getDeposit(), deal.getCashmoney());
						i++;
					}
				}

			}
		}
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		Data.pause();
	}
}