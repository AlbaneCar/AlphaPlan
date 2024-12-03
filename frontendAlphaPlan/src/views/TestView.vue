<template>
  <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG">
    <Title>Page de test</Title>

    <!-- Contenu de la page de test -->
     <LineUpOverview />
    <!-- <Sparkles />
    <Hourglass />
    <ArrowLeftRight /> -->
  </div>
</template>

<script setup>
import LineUpOverview from '../components/LineUpChange/LineUpOverview.vue';
import Title from '../components/Title.vue';
/* ###################################### */
import { Sparkles, Hourglass, ArrowLeftRight } from 'lucide-vue-next';
</script>