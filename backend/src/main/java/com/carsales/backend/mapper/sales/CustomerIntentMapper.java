package com.carsales.backend.mapper.sales;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper
public interface CustomerIntentMapper {

    Integer selectCustomerIdByPhone(@Param("phone") String phone);

    Integer selectCustomerIdByIdCard(@Param("idCard") String idCard);

    ExistingCustomerSnapshot selectCustomerSnapshotById(@Param("customerId") Integer customerId);

    Integer insertCustomerIfAbsent(
            @Param("customerName") String customerName,
            @Param("gender") String gender,
            @Param("phone") String phone,
            @Param("idCard") String idCard,
            @Param("address") String address,
            @Param("firstVisitDate") LocalDate firstVisitDate,
            @Param("sourceChannel") String sourceChannel
    );

    Integer selectModelId(@Param("modelId") Integer modelId);

    Integer selectStaffId(@Param("staffId") Integer staffId);

    Integer insertCustomerIntent(
            @Param("customerId") Integer customerId,
            @Param("modelId") Integer modelId,
            @Param("intentLevel") String intentLevel,
            @Param("remark") String remark,
            @Param("staffId") Integer staffId,
            @Param("nextContactTime") LocalDateTime nextContactTime
    );

    class ExistingCustomerSnapshot {
        private String idCard;
        private String customerName;
        private String gender;
        private String address;
        private String sourceChannel;

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSourceChannel() {
            return sourceChannel;
        }

        public void setSourceChannel(String sourceChannel) {
            this.sourceChannel = sourceChannel;
        }
    }
}
