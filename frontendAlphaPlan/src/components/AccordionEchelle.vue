<template>
  <div class="h-full w-full">

    <template v-if="echellesNote.length === 0">
      <div class="flex flex-col justify-center items-center gap-4 h-full w-full">
        <Illustration :nameImage="'nothing2.svg'" :width="300"/>
        <p class="font-CASlalomBold text-center">Aucune échelle de notes n'a été créée.<br>Veuillez en créer une.</p>
      </div>
    </template>

    <template v-else>
      <Accordion v-if="echellesNote" :multiple="true" :activeIndex="[]" class="flex flex-col gap-2">
        <AccordionTab v-for="echelle in echellesNote" :header="echelle.typeEchelle">
          <div>
            <p class="m-0 font-Roboto">
            <div class="flex justify-start items-center bg-custom-sideBarBG rounded-md shadow-sm p-2 border-gray-200 border text-sm gap-2">
              <Info class="text-custom-lightblueAP w-4 h-4" />
              <p class="m-0">{{ echelle.description }}</p>
            </div>
            <br />
            <div class="w-full px-4">
              <Echelle :echelons="echelle.echelons" :typeEchelle="echelle.typeEchelle" />
            </div>
            <div class="mt-4">
              <PopUpBoutonSupEchelle :id="echelle.id" />
            </div>
            </p>
          </div>
        </AccordionTab>
      </Accordion>
    </template>
  </div>
</template>

<script setup>
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import { ref, onMounted } from 'vue';
import { fetchEchellesNote } from '../services/EchelleNoteService.js';
import PopUpBoutonSupEchelle from './PopUpBoutonSupEchelle.vue';
import Echelle from './Echelle.vue';
import { Info } from 'lucide-vue-next';
import Illustration from './NoDataIllustration/Illustration.vue';

const echellesNote = ref([]);

onMounted(async () => {
  echellesNote.value = await fetchEchellesNote();
});

</script>

<style scoped>
:deep(.p-accordion-content) {
  background-color: white;
  border-radius: 0.375rem;
}

:deep(.p-accordion-tab) {
  background-color: white;
  border-radius: 0.375rem;
  border: 1px solid rgb(229 231 235);
}

:deep(.p-accordion-header-link) {
  background-color: white;
}

.note-container {
  margin: 0;
  margin-bottom: 10px;
}

.note {
  font-family: 'Roboto', sans-serif;
}

.accordion-with-border {
  margin-bottom: 10px;
}

.description-container {
  border: 2px solid var(--background-color, #7A7EFF);
  padding: 5px;
  color: white;
  margin-bottom: 10px;
  border-radius: 8px;
  display: inline-block;
  font-size: 18px;
  line-height: 1.5;
}


.bg-custom-darkblueAP {
  background-color: #003366;
}
</style>
