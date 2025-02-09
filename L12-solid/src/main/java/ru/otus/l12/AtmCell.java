package ru.otus.l12;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtmCell {

    int nominal;
    int amount;
}