/**

Material design: [Chips](https://material.io/guidelines/components/chips.html#chips-specs)

`paper-chip`
Paper-chip's represent complex entities in small blocks, such as a contact.

### Styling
The following custom properties and mixins are available for styling:

Custom property | Description | Default
----------------|-------------|----------
`--paper-chip-label-color` | The paper-chip label-color | `rgba(0, 0, 0, 0.6)`
`--paper-chip-active-background-color` | The paper-chip active background-color | `#d6d6d6`
`--paper-chip-background-color` | The paper-chip background-color | `#e4e4e4`
 `--paper-chip-selected-background-color` | The paper-chip background-color | `#535353`
`--paper-chip-avatar-background-color` | The paper-chip avatar background-color | `#757575`
`--paper-chip-avatar-font-color` | The paper-chip avatar font and icon color | `#ffffff`
`--paper-chip-close-color` | The paper-chip close icon color | `#a6a6a6`
`--paper-chip-font-size` | The paper-chip font size | `13px`
`--paper-chip-font-family` | The paper-chip font size | `"Roboto", sans-serif`
`--paper-chip-close-label` | Mixin for the paper-chip close label | `{}`
`--paper-chip` | Mixin for the paper-chip | `{}`

@element paper-chip
@demo demo/index.html
*/
import '@polymer/polymer/polymer-legacy.js';

import './skip-loading-font-roboto.js';
import '@polymer/paper-styles/paper-styles.js';
import '@polymer/iron-icons/iron-icons.js';
import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';

class PaperChip extends PolymerElement {

    static get is() {
				return 'paper-chip';
    }

    static get properties() {
				return {

            /**
            * The label for this paper-chip. The default value is 'Default Label'.
            */
            label: {
                type: String,
                value: 'Default Label'
            },
            noAvatar: {
                type: Boolean,
                notify: true,
                reflectToAttribute: true
            },

            /**
            * If true, the paper-chips can be closed.
            */
            closable: {
                type: Boolean,
                value: false
            },

            /**
            * If true, the element will not produce a hover effect.
            */
            noHover: {
                type: Boolean,
                value: false
            },
            noAutoDomRemoval: {
               type: Boolean,
               value: false,
               notify: true
            }
				};
    }

    static get template() {
        return html`
        <style>
        
        
            .chip {
                font-family: var(--paper-chip-font-family, "Roboto", sans-serif);
                display: inline-block;
                height: 32px;
                font-size: var(--paper-chip-font-size, 13px);
                font-weight: 500;
                color: var(--paper-chip-label-color, rgba(0, 0, 0, 0.6));
                line-height: 32px;
                padding: 0 4px 0 12px;
                border-radius: 16px;
                background-color: var(--paper-chip-background-color, #E0E0E0);
                margin-bottom: 5px;
                margin-right: 5px;
                @apply --paper-chip;
            }
            .chip .closeIcon {
                margin-left: 4px;
                cursor: pointer;
                float: right;
                width: 12px;
            }
            .chip .inline {
                display: -webkit-inline-box;
            }
            .hoverEffect:hover {
                @apply --shadow-elevation-2dp;
                cursor: default;
            }
            .unselectable {
                -webkit-touch-callout: none;
                -webkit-user-select: none;
                -khtml-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
            }
            .close {
                top: -1px;
                @apply --paper-chip-close-label;
            }
            iron-icon {
                --iron-icon-height: 16px;
                --iron-icon-width: 16px;
                position: relative;
                right: 8px;
                color: var(--paper-chip-background-color, #E0E0E0);
                background-color: var(--paper-chip-close-color, #A6A6A6);
                border-radius: 50%;
            }
            .label {
                margin-right: 12px;
            }
            :host([no-avatar]) .avatar {
              display: none;
            }
            :host(.no-avatar) .avatar {
              display: none;
            }
            .avatar ::slotted(.chip-image) {
                float: left;
                margin: 0 8px 0 -12px;
                height: 32px;
                width: 32px;
                border-radius: 50%;
            }
            .avatar ::slotted(.chip-background) {
                --iron-icon-height: 19px;
                --iron-icon-width: 19px;
                background: var(--paper-chip-avatar-background-color, #989898);
                border-radius: 50%;
                color: var(--paper-chip-avatar-font-color, #ffffff);
                float: left;
                font-weight: bold;
                font-size: 16px;
                height: 32px;
                margin: 0 8px 0 -12px;
                text-align: center;
                width: 32px;
            }
            [hidden] {
                display: none;
            }
        </style>

        <div on-click="_clicked" class\$="[[_computePaperChipClass(noHover, selected)]]">
            <span class="avatar"><slot name="avatar"></slot></span>
            <span class="label">[[label]]</span>
            <div hidden\$="[[!closable]]" class="closeIcon" on-click="_remove">
                <iron-icon class="close" icon="icons:clear"></iron-icon>
            </div>
        </div>`
    }

    _computePaperChipClass(noHover, selected) {
      let classes = 'chip unselectable ';
				if (!noHover) {
             classes += ' hoverEffect ';
				}
				if(selected) {
				  classes += ' selected '
        }
        return classes;
    }

    _remove(event) {
			event.stopPropagation();
			this._dispatch('chip-removed');
				if (!this.noAutoDomRemoval && this.parentNode.id != 'slot2' && this.parentNode.querySelector("dom-repeat") === null) {
            this.parentNode.removeChild(this);
				}
    }
	
	_clicked(event){
		event.stopPropagation();
		this._dispatch('chip-clicked');
	}

	_dispatch(eventName){
		this.dispatchEvent(new CustomEvent(eventName, {
            detail: {
                'chipLabel': this.label
            },
            composed: true,
            bubbles: true
				}));
	}
}

window.customElements.define(PaperChip.is, PaperChip);

export default PaperChip;