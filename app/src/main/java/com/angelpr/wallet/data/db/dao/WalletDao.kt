package com.angelpr.wallet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.angelpr.wallet.data.db.entities.CardWalletEntity
import com.angelpr.wallet.data.db.entities.DebtsWalletEntity

@Dao
interface WalletDao {

    // Card into the database
    @Query("SELECT * FROM cardWallet_table")
    suspend fun getAllCards(): List<CardWalletEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardWalletEntity)

    @Update
    suspend fun updateCard(card: CardWalletEntity)

    @Query("DELETE FROM cardWallet_table WHERE id = :id")
    suspend fun deleteCard(id: Int)

    // Debt Card into the database
    @Query("SELECT * FROM debtsWallet_table WHERE idWallet = :idCard AND isPaid = 0")
    suspend fun getDebtNotPaidCardById(idCard: Int): List<DebtsWalletEntity>

    @Query("SELECT * FROM debtsWallet_table WHERE idWallet = :idCard AND isPaid = 1 ORDER BY id DESC LIMIT :limit")
    suspend fun getDebtPaidCardById(idCard: Int, limit: Int): List<DebtsWalletEntity>

    @Query("SELECT SUM(CASE WHEN quotas > 1 THEN debt/quotas ELSE debt END) AS total_debt FROM debtsWallet_table WHERE idWallet = :idCard AND date BETWEEN :dateInit AND :dateEnd")
    suspend fun getLineUseCard(idCard: Int, dateInit: Long, dateEnd: Long): Float

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebtCard(debtCard: DebtsWalletEntity)

    @Query("UPDATE debtsWallet_table SET isPaid = :isPaid, quotePaid = :quotasPaid WHERE id = :id")
    suspend fun updateDebtCard(id: Int, quotasPaid: Int, isPaid: Int)

    @Query("DELETE FROM debtsWallet_table WHERE id = :id")
    suspend fun deleteDebtCard(id: Int)

    @Query("DELETE FROM debtsWallet_table WHERE idWallet = :idCard")
    suspend fun deleteAllDebtCard(idCard: Int)


}