<template>
<div class="w-full flex justify-between">
    <div class="w-[70%] h-full flex flex-col justify-between">
        <!-- Première section : Team Work -->
        <TabNotes :icon="'te_wo.svg'" :noms="nomsSection1.value" :notes="notesSection1.value" :coeffs="coeff1.value"/>
        <!-- Deuxième section : Individual Sprint -->
        <TabNotes :icon="'in_sp.svg'" :noms="nomsSection2.value" :notes="notesSection2.value" :coeffs="coeff2.value"/>
        <!-- Troisième section : Individual Presentation -->
        <TabNotes :icon="'in_pr.svg'" :noms="nomsSection3.value" :notes="notesSection3.value" :coeffs="coeff3.value"/>
        <!-- Quatrième section : Individual Global Sprint -->
        <TabNotes :icon="'ig_sp.svg'" :noms="nomsSection4.value" :notes="notesSection4.value" :coeffs="coeff4.value"/>
    </div>
</div>
</template>


<script setup>

</script>