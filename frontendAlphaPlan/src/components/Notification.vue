<script setup>
import { X } from 'lucide-vue-next';


const props = defineProps({
    notification: Array,
});

const emits = defineEmits(['deleteNotification']);

const user_id = localStorage.getItem('user_id');
</script>

<template>
    <div class="flex flex-col justify-start items-start w-full p-2 rounded-md bg-custom-sideBarBG shadow-sm border border-gray-200">
        <div class="flex justify-between items-start w-full ">
            <div class="text-xs font-CASlalomBold text-custom-darkblueAP">
                <span class="uppercase">{{ notification.envoyeur.nom }}</span> 
                &nbsp;{{ notification.envoyeur.prenom }}
            </div>
            <div class="flex gap-2">
                <div
                    class="flex justify-center items-center text-[10px] uppercase font-CASlalomBold text-white bg-red-600 rounded-sm px-1 animate-pulse">
                    {{ notification.type }}
                </div>
                <div v-tooltip.top="{
                        value: `Supprimer`,
                        pt: {
                            arrow: {
                                style: {
                                    borderBottomColor: 'var(--danger-color)'
                                }
                            },
                            text: 'bg-danger font-Roboto text-xs py-1'
                        }
                    }" @click="$emit('deleteNotification', notification)">
                    <X class="w-3 h-4 text-custom-darkblueAP hover:text-custom-red transition-colors" />
                </div>
            </div>

        </div>
        <div class="flex justify-start items-start w-full font-Roboto text-custom-grey text-xs italic mt-2">
            {{ notification.message }}
        </div>
    </div>
</template>