<script setup>
import Dialog from 'primevue/dialog';
import BoutonExcel from '../BoutonExcelPL.vue';
import { ref, watch } from 'vue';
import { defineProps } from 'vue';


const visible = ref(false);

const props = defineProps({
  visibilite: Boolean
});

const emits = defineEmits(['update:visibilite']);


watch(() => props.visibilite, (newValue) => {
  visible.value = !visible.value;
});

</script>

<template>
  <div class="card flex justify-content-center">
    <Dialog v-model:visible="visible" modal header="Confirmer la publication" class="min-w-[500px]">
        <Button @click="sendTeams('PUBLIE')" classes="p-2">Publier</Button>
        <Button @click="!visible.value" classes="p-2">Annuler</Button>
    </Dialog>
  </div>
</template>