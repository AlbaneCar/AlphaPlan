<template>
    <SignalementForm :equipe="equipe"/>
</template>

<script setup>
import SignalementForm from '../components/SignalementForm.vue';
import { ref } from 'vue';

const equipe = ref({
    id: 1,
    nom: 'Spica'
});
</script>


<style scoped>
</style>