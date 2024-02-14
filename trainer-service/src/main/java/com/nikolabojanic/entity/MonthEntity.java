package com.nikolabojanic.entity;

import java.time.Month;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthEntity {
    private Month month;
    private Double trainingSummary;
}
