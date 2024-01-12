package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Customer {
    private String cusId;
    private String cusName;
    private String cusAddress;
    private double cusSalary;
}
