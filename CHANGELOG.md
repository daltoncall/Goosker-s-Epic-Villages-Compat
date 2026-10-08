# Changelog

## 1.1.5-alpha

- Added `50_50` village selection for Plains, Snowy, and Taiga.
- Kept each generated village internally consistent: Epic starts continue through Epic/vanilla-replaced pools, while Luki starts continue through `revampedvillages:*` pools.
- Added centerless handling for the Luki half of Plains when `epic_plains=50_50` and `no_center_structure_gen=true`.
- Kept Desert and Savanna on Luki's Grand Capitals, with optional center removal.
- Condensed configuration to `epic_plains`, `epic_snowy`, `epic_taiga`, and `no_center_structure_gen`.
- Does not alter Structurify configuration.
