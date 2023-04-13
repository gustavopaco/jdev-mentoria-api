package com.pacoprojects.repository;

import com.pacoprojects.dto.projections.VendaCompraProjectionSelected;
import com.pacoprojects.model.VendaCompra;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface VendaCompraRepository extends JpaRepository<VendaCompra, Long> {



    Optional<VendaCompraProjectionSelected> findVendaCompraById(Long id);

    List<VendaCompraProjectionSelected> findAllByEmpresa_Id(Long idEmpresa);

    @Modifying
    @Query(value = "UPDATE VendaCompra set enabled = false where id = ?1")
    void softDelete(Long id);

    @Modifying
    @Query(value = "UPDATE VendaCompra set enabled = true where id = ?1")
    void enableVenda(Long id);


    @Modifying
    @Query(nativeQuery = true,
            value = " DELETE FROM item_venda_compra WHERE venda_compra_id =:id ;" +
                    " UPDATE venda_compra v SET nota_fiscal_venda_id = NULL WHERE v.id = :id ; " +
                    " DELETE FROM nota_fiscal_venda WHERE venda_compra_id = :id ; " +
                    " DELETE FROM status_rastreio WHERE venda_compra_id = :id ; " +
                    " DELETE FROM venda_compra WHERE id = :id ; ")
    void deleteVendaCompraByIdNative(@Param("id") Long id);


    default void deleteVendaCompraAndAssociatedEntities(Long id) {
        queryDeleteItemVendaCompraById(id);
        queryUpdateVendaCompraById(id);
        queryDeleteNotaFiscalVendaById(id);
        queryDeleteStatusRastreioById(id);
        queryDeleteVendaCompraById(id);
    }

    @Modifying
    @Query(value = "DELETE FROM ItemVendaCompra i WHERE i.vendaCompra.id = :id ")
    void queryDeleteItemVendaCompraById(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE VendaCompra v SET v.notaFiscalVenda = NULL WHERE v.id = :id ")
    void queryUpdateVendaCompraById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE NotaFiscalVenda n WHERE n.vendaCompra.id = :id ")
    void queryDeleteNotaFiscalVendaById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE StatusRastreio s WHERE s.vendaCompra.id = :id ")
    void queryDeleteStatusRastreioById(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE VendaCompra v WHERE v.id = :id ")
    void queryDeleteVendaCompraById(@Param("id") Long id);
}
