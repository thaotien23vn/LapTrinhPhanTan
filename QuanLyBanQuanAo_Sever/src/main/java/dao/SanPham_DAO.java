package dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import dao_interface.SanPham_DAO_Interface;
import entities.KhachHang;
import entities.SanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class SanPham_DAO extends UnicastRemoteObject implements SanPham_DAO_Interface {
    
	 private EntityManager entityManager;

	    public SanPham_DAO() throws RemoteException {
	        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mssql");
	        entityManager = entityManagerFactory.createEntityManager();
	    }

    public boolean create(SanPham sanpham) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(sanpham);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
    public boolean delete(String maSanPham) {
        try {
        	SanPham sanpham = entityManager.find(SanPham.class, maSanPham);
            if (sanpham != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(sanpham);
                entityManager.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

	@Override
	 public boolean update(SanPham sanpham) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(sanpham);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
	public boolean updateTrangThaiSanPham(SanPham sanpham) {
        try {
            entityManager.getTransaction().begin();
            sanpham.setTrangThai(false); 
            entityManager.merge(sanpham);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateKH(KhachHang kh) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(kh);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
