package com.leon.services;

import com.leon.models.Instrument;

import java.util.List;

public interface InstrumentService
{
    List<Instrument> getAll();
    void reconfigure();
}
