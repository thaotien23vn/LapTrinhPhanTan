package dao_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import entities.SanPham;

public interface SanPham_DAO_Interface extends Remote {
    
    boolean create(SanPham sanpham) throws RemoteException;
    
    boolean update(SanPham sanpham) throws RemoteException;
    
    boolean delete(String maSanPham) throws RemoteException;
    
}
