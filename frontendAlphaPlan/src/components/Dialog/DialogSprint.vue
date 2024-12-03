<script setup>
import Dialog from 'primevue/dialog';
import { ref, watch, defineProps } from 'vue';

const visible = ref(false);
const numberOfSprintsInput = ref(0);


const props = defineProps({
    visibilite: Boolean
});

const emits = defineEmits(['update:visibilite', 'close']);

watch(() => props.visibilite, (newValue) => {
    visible.value = newValue;
});

const closeDialog = () => {
    visible.value = false;
    emits('update:visibilite', false);
    emits('close', numberOfSprintsInput.value);
};

</script>

<template>
    <div>
        <Dialog v-model:visible="visible" modal header="Entrez le nombre de sprints" class="min-w-[500px]">
            <div class="flex align-items-center">
                <input type="number" v-model="numberOfSprintsInput" min="1"
                    class="border-[1px] border-black focus:outline-none focus:border-custom-lightblueAP transition-colors text-gray-500 rounded-md px-2 py-1 font-Roboto mb-2 mx-2" />
                <button @click="closeDialog"
                    class="bg-custom-lightblueAP rounded-md text-white border-none px-2 py-1 mb-2 font-CASlalomBold hover:bg-custom-darkblueAP duration-300  hover:transition-colors">Valider</button>
            </div>
        </Dialog>
    </div>
</template>