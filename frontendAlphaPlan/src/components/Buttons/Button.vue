<script setup>
import FolderDown from '../../assets/icons/FolderDown.vue';
import FolderUp from '../../assets/icons/FolderUp.vue';
import Edit from '../../assets/icons/Edit.vue';
import Save from '../../assets/icons/Save.vue';
import Validate from '../../assets/icons/Validate.vue'
import Plus from '../../assets/icons/Plus.vue';
import Send from '../../assets/icons/Send.vue';
import { CircleCheckBig } from 'lucide-vue-next';

const props = defineProps({
    icon : String,
    classes: {
        type: String,
        default: '' // Par défaut, aucune classe supplémentaire
    }
})

</script>

<template>
    <button :class="['bg-[#1A78D1]  hover:bg-custom-darkblueAP text-white font-CASlalomBold rounded-md duration-300', props.classes]">
        <div class="flex justify-center items-center gap-2">
            <span v-if="props.icon == 'import'"><FolderDown color="white" :width="'18'" /></span>
            <span v-else-if="props.icon == 'export'"><FolderUp color="white" :width="'18'" /></span>
            <span v-else-if="props.icon == 'edit'"><Edit color="white" :width="'18'" /></span>
            <span v-else-if="props.icon == 'validate'"><Validate color="white" :width="'18'" /></span>
            <span v-else-if="props.icon == 'save'"><Save color="white" :width="'18'" /></span>
            <span v-else-if="props.icon == 'plus'"><Plus color="white" :width="'18'" /></span>
            <span v-else-if="props.icon == 'send'"><Send color="white" :width="'18'" /></span>
           <div class="leading-5">
                <slot />
           </div>
        </div>
    </button>
</template>