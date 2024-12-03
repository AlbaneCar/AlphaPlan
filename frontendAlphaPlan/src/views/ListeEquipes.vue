<template>
    <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG">
      <Title title="Liste des équipes"></Title>
      <EquipesListe />
    </div>
</template>

<script setup>
import EquipesListe from '../components/EquipesListe.vue';
import Title from '../components/Title.vue';
</script>
