package com.cg.model;

import com.cg.model.dto.LocationRegionDTO;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "location_region")
public class LocationRegion extends BaseEntities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "province_id")
    private String provinceId;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "district_id")
    private String districtId;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "ward_id")
    private String wardId;

    @Column(name = "ward_name")
    private String wardName;

    private String address;

    @OneToOne(mappedBy = "locationRegion")
    private Customer customer;
    public LocationRegionDTO toLocationRegionDTO() {
        return new LocationRegionDTO()
                .setId(id)
                .setProvinceId(provinceId)
                .setDistrictId(districtId)
                .setDistrictName(districtName)
                .setWardId(wardId)
                .setWardName(wardName)
                .setAddress(address);
    }

}
