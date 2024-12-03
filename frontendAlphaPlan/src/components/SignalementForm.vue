<template>
    <div class="card flex justify-content-center">
        <Dialog v-model:visible="visible" modal :header="'Signalement : ' + nomEquipe" class="min-w-[550px] min-h-[300px]">
            <div class="flex flex-col justify-start items-center p-2 gap-3">
                <textarea v-model="comment"
                    class="w-[500px] h-[100px] w-full px-4 py-2 mt-2 font-CASlalomBold text-neutral-400 italic text-sm font-normal font-Roboto border border-gray-300 rounded-lg h-20 resize-none"
                    placeholder="Raisons du Signalement"
                    >
                </textarea>

                <Button v-if="!isLoadding" icon="save" class="mt-4 py-2 px-4" @click="saveSignalement">Enregistrer le Signalement</Button>
                <LoadingButton v-if="isLoadding" icon="save" class="mt-4 py-2 px-4" @click="saveSignalement">Enregistrer le Signalement</LoadingButton>

            </div>
        </Dialog>
    </div>
</template>

<script setup>
import Dialog from 'primevue/dialog';
import { ref, defineProps, watch, onMounted } from 'vue';
import Button from '../components/Buttons/Button.vue';
import LoadingButton from '../components/Buttons/LoadingButton.vue';
import { createSignalement } from '../services/SignalementService';
import { useToast } from 'vue-toastification';

const toast = useToast();

const props = defineProps({
    equipe: {
        type: Object,
        required: true
    },
    visibilite: {
        type: Boolean,
        default: false
    }
});

const nomEquipe = ref('');
const equipeId = ref(null);
const comment = ref('');
const erreurMessage = ref('');
const okMessage = ref('');
const visible = ref(props.visibilite);
const isLoadding = ref(false);
const isError = ref(false);


watch(() => props.visibilite, (value) => {
    visible.value = value;
});

watch(()=> props.equipe, (value) => {
    nomEquipe.value = value.nom;
    equipeId.value = value.id;
});

const saveSignalement = async () => {
    try {
        isLoadding.value = true;
        await createSignalement(comment.value, equipeId.value, localStorage.getItem('user_id'))
        toast.success("Signalement enregistré avec succès.", {
            position: "top-center",
        });
    } catch (error) {
        toast.error("Une erreur s'est produite. Veuillez réessayer plus tard.", {
            position: "top-center",
        });
    } finally {
        isLoadding.value = false;
    }
}

</script>


<style scoped>
</style>