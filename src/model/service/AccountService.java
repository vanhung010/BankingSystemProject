package model.service;

import model.data.AccountDao;
import model.data.UserDao;
import model.entity.Account;
import model.entity.Customer;

import java.util.List;

public class AccountService {

    private UserDao userDao = new UserDao();
    private AccountDao accountDao = new AccountDao();

    public Customer getCustomerbyId(int idCus){
        Customer customer = userDao.getCustomerById(idCus);
        if(customer == null){
            throw new RuntimeException("Không tìm thấy khách hàng");
        }
        return customer;
    }

    public List<Account> getAllAccount(int idCustomer) {
        return accountDao.getAllAccountOfCustomerDao(idCustomer);
    }
}
