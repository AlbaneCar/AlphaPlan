<script setup>
import Dialog from 'primevue/dialog';
import BoutonExcel from '../BoutonExcelPL.vue';
import { ref, watch } from 'vue';
import { defineProps } from 'vue';
import ProgressSpinner from 'primevue/progressspinner';
import Coefs from '../Coefs.vue';
import Loader from '../Loader/Loader.vue';

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
        <Dialog v-model:visible="visible" modal header="Modifier les coefficients" class="min-w-[500px]">
            <div class="flex flex-col justify-start items-center p-2 gap-3">
                <Coefs />
            </div>
        </Dialog>
    </div>
</template>