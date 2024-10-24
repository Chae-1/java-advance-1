package thread.sync;

public class WithdrawTask implements Runnable {

    private final BankAccount account;
    private final int amount;

    public WithdrawTask(BankAccount account, int amount) {
        this.amount = amount;
        this.account = account;
    }

    @Override
    public void run() {
        account.withdraw(amount);
    }
}
