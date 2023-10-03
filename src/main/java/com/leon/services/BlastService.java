package com.leon.services;

import com.leon.models.Blast;

import java.util.List;

public interface BlastService
{
    void reconfigure();

    Blast saveBlast(Blast blastToSave);

    void deleteBlast(String blastId);

    Blast updateBlast(Blast blastToUpdate);

    List<Blast> getBlasts();
}
